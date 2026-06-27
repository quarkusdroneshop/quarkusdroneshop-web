package io.quarkusdroneshop.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.domain.OrderStatus;
import io.quarkusdroneshop.domain.valueobjects.LoyaltyUpdate;
import io.quarkusdroneshop.web.domain.DashboardUpdate;
import io.quarkusdroneshop.web.domain.RewardEvent;
import io.quarkusdroneshop.web.infrastructure.testsupport.KafkaTestResource;
import io.restassured.response.Response;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-End test: HTTP request → OrderService → Kafka message → SSE stream
 */
@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
class OrderFlowE2ETest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderFlowE2ETest.class);

    @Inject
    @Any
    InMemoryConnector connector;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testFullOrderFlow_standardCustomer() throws Exception {
        InMemorySink<String> ordersSink = connector.sink("orders-up");
        int before = ordersSink.received().size();

        // Step 1: POST /order
        String payload = "{"
            + "\"id\":\"e2e-order-001\","
            + "\"storeId\":\"ATLANTA\","
            + "\"orderSource\":\"WEB\","
            + "\"rewardsId\":\"alice\","
            + "\"qdca10Items\":[{\"item\":\"QDC_A101\",\"price\":135.50,\"name\":\"Alice\"}],"
            + "\"qdca10proItems\":[],"
            + "\"total\":135.50"
            + "}";

        Response response = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(payload)
            .post("/order");

        // Step 2: Verify 202 response
        assertEquals(202, response.statusCode());
        LOGGER.info("Order accepted, body: {}", response.body().asString());

        String body = response.body().asString();
        JsonNode bodyJson = mapper.readTree(body);
        assertNotNull(bodyJson.get("order"));
        assertEquals("e2e-order-001", bodyJson.get("order").get("orderId").asText());
        assertNull(bodyJson.get("reward"));

        // Step 3: Verify Kafka message sent
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
            .until(() -> ordersSink.received().size() > before);

        String kafkaMsg = ordersSink.received().get(before).getPayload();
        LOGGER.info("Kafka message: {}", kafkaMsg);
        JsonNode kafkaJson = mapper.readTree(kafkaMsg);
        assertEquals("e2e-order-001", kafkaJson.get("id").asText());
        assertEquals("WEB", kafkaJson.get("orderSource").asText());
        assertEquals("ATLANTA", kafkaJson.get("location").asText());
        assertEquals("alice", kafkaJson.get("loyaltyMemberId").asText());
    }

    @Test
    void testFullOrderFlow_fiveOrMoreItems_triggerReward() throws Exception {
        InMemorySink<String> ordersSink = connector.sink("orders-up");
        int before = ordersSink.received().size();

        // 5 items to trigger reward
        String payload = "{"
            + "\"id\":\"e2e-order-002\","
            + "\"storeId\":\"RALEIGH\","
            + "\"orderSource\":\"WEB\","
            + "\"rewardsId\":\"bob\","
            + "\"qdca10Items\":["
            + "  {\"item\":\"QDC_A101\",\"price\":100.0,\"name\":\"Bob\"},"
            + "  {\"item\":\"QDC_A102\",\"price\":100.0,\"name\":\"Bob\"},"
            + "  {\"item\":\"QDC_A103\",\"price\":100.0,\"name\":\"Bob\"},"
            + "  {\"item\":\"QDC_A104_AC\",\"price\":100.0,\"name\":\"Bob\"},"
            + "  {\"item\":\"QDC_A104_AT\",\"price\":100.0,\"name\":\"Bob\"}"
            + "],"
            + "\"qdca10proItems\":[],"
            + "\"total\":500.0"
            + "}";

        Response response = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(payload)
            .post("/order");

        assertEquals(202, response.statusCode());

        String body = response.body().asString();
        JsonNode bodyJson = mapper.readTree(body);
        assertNotNull(bodyJson.get("order"));
        assertNotNull(bodyJson.get("reward"), "reward should be present for 5+ items");
        LOGGER.info("Reward received: {}", bodyJson.get("reward"));

        // Verify Kafka message
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
            .until(() -> ordersSink.received().size() > before);
        String kafkaMsg = ordersSink.received().get(before).getPayload();
        LOGGER.info("Kafka message for reward order: {}", kafkaMsg);
        assertNotNull(kafkaMsg);
    }

    @Test
    void testFullOrderFlow_dashboardUpdatePropagation() {
        // Simulate incoming Kafka dashboard update (as would come from barista service)
        UUID orderId = UUID.fromString("e2e0da5b-0000-0000-0000-000000000001");
        UUID itemId = UUID.randomUUID();
        DashboardUpdate update = new DashboardUpdate(orderId, itemId, "Carol", "QDC_A101", OrderStatus.READY, "barista-bot");

        @SuppressWarnings("unchecked")
        InMemorySource<DashboardUpdate> webUpdates = (InMemorySource<DashboardUpdate>)(InMemorySource<?>)connector.source("web-updates");
        webUpdates.send(update);

        LOGGER.info("Dashboard update published for order: {}", orderId);
        // SSE stream would deliver this to the browser; endpoint existence verified by DashboardResourceTest
    }

    @Test
    void testFullOrderFlow_rewardEventPropagation() {
        // Simulate incoming reward event from external rewards service
        RewardEvent reward = new RewardEvent("alice", "e2e-order-001", BigDecimal.valueOf(20.25));

        @SuppressWarnings("unchecked")
        InMemorySource<RewardEvent> rewardSource = (InMemorySource<RewardEvent>)(InMemorySource<?>)connector.source("rewards");
        rewardSource.send(reward);

        LOGGER.info("RewardEvent propagated for: {}", reward.customerName);
    }

    @Test
    void testFullOrderFlow_loyaltyUpdatePropagation() {
        LoyaltyUpdate loyaltyUpdate = new LoyaltyUpdate("carol@example.com", "1000pts");

        @SuppressWarnings("unchecked")
        InMemorySource<LoyaltyUpdate> loyaltySource = (InMemorySource<LoyaltyUpdate>)(InMemorySource<?>)connector.source("loyalty-updates");
        loyaltySource.send(loyaltyUpdate);

        LOGGER.info("LoyaltyUpdate propagated for: {}", loyaltyUpdate.getEmail());
    }

    @Test
    void testFullOrderFlow_proCustomer() throws Exception {
        InMemorySink<String> ordersSink = connector.sink("orders-up");
        int before = ordersSink.received().size();

        String payload = "{"
            + "\"id\":\"e2e-order-003\","
            + "\"storeId\":\"ATLANTA\","
            + "\"orderSource\":\"WEB\","
            + "\"rewardsId\":\"proCustomer\","
            + "\"qdca10Items\":[],"
            + "\"qdca10proItems\":[{\"item\":\"QDC_A105_Pro01\",\"price\":550.0,\"name\":\"proCustomer\"}],"
            + "\"total\":550.0"
            + "}";

        Response response = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(payload)
            .post("/order");

        assertEquals(202, response.statusCode());

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
            .until(() -> ordersSink.received().size() > before);

        String kafkaMsg = ordersSink.received().get(before).getPayload();
        JsonNode kafkaJson = mapper.readTree(kafkaMsg);
        assertEquals("e2e-order-003", kafkaJson.get("id").asText());
        assertEquals(1, kafkaJson.get("qdca10proLineItems").size());
        LOGGER.info("Pro order flow completed: {}", kafkaMsg);
    }
}
