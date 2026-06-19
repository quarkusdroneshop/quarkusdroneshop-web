package io.quarkusdroneshop.web.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.domain.Item;
import io.quarkusdroneshop.domain.OrderLineItem;
import io.quarkusdroneshop.domain.OrderSource;
import io.quarkusdroneshop.domain.valueobjects.OrderResult;
import io.quarkusdroneshop.domain.valueobjects.Qdca10Result;
import io.quarkusdroneshop.domain.valueobjects.Qdca10proResult;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import io.quarkusdroneshop.web.infrastructure.testsupport.KafkaTestResource;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
class OrderServiceTest {

    @Inject
    @Any
    InMemoryConnector connector;

    @Inject
    OrderService orderService;

    private InMemorySink<String> sink;
    private int baseSize;

    @BeforeEach
    void setUp() {
        sink = connector.sink("orders-up");
        baseSize = sink.received().size();
    }

    @Test
    void testSingleItemOrder_returnsQdca10Result() throws Exception {
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "order-a", "ATLANTA", OrderSource.WEB, "alice",
            List.of(new OrderLineItem(Item.QDC_A101, BigDecimal.valueOf(135.50), "Alice")),
            null, BigDecimal.valueOf(135.50)
        );

        OrderResult result = orderService.placeOrder(cmd).get();

        assertInstanceOf(Qdca10Result.class, result);
        assertNotNull(result.getOrderUp());
        assertNull(result.getRewardEvent());
        assertEquals(baseSize + 1, sink.received().size());
    }

    @Test
    void testFiveItemOrder_triggersReward() throws Exception {
        List<OrderLineItem> items = Arrays.asList(
            new OrderLineItem(Item.QDC_A101, BigDecimal.valueOf(100), "Bob"),
            new OrderLineItem(Item.QDC_A102, BigDecimal.valueOf(100), "Bob"),
            new OrderLineItem(Item.QDC_A103, BigDecimal.valueOf(100), "Bob"),
            new OrderLineItem(Item.QDC_A104_AC, BigDecimal.valueOf(100), "Bob"),
            new OrderLineItem(Item.QDC_A104_AT, BigDecimal.valueOf(100), "Bob")
        );
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "order-b", "ATLANTA", OrderSource.WEB, "bob", items, null, BigDecimal.valueOf(500)
        );

        OrderResult result = orderService.placeOrder(cmd).get();

        assertInstanceOf(Qdca10Result.class, result);
        assertNotNull(result.getRewardEvent());
        assertNotNull(result.getOrderUp().getRewardPoints());
        assertEquals(BigDecimal.valueOf(500).multiply(BigDecimal.valueOf(0.15)), result.getRewardEvent().getPoints());
        assertEquals(baseSize + 1, sink.received().size());
    }

    @Test
    void testProCustomer_returnsQdca10proResult() throws Exception {
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "order-c", "RALEIGH", OrderSource.WEB, "proUser",
            List.of(new OrderLineItem(Item.QDC_A105_Pro01, BigDecimal.valueOf(500), "proUser")),
            null, BigDecimal.valueOf(500)
        );

        OrderResult result = orderService.placeOrder(cmd).get();

        assertInstanceOf(Qdca10proResult.class, result);
        assertNotNull(result.getOrderUp());
        assertEquals("proUser", result.getOrderUp().getCustomerName());
        assertEquals(baseSize + 1, sink.received().size());
    }

    @Test
    void testProItemsInProList_returnsQdca10proResult() throws Exception {
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "order-d", "ATLANTA", OrderSource.WEB, "proMember",
            null,
            List.of(new OrderLineItem(Item.QDC_A105_Pro02, BigDecimal.valueOf(600), "proMember")),
            BigDecimal.valueOf(600)
        );

        OrderResult result = orderService.placeOrder(cmd).get();

        assertInstanceOf(Qdca10proResult.class, result);
        assertEquals(baseSize + 1, sink.received().size());
    }

    @Test
    void testEmptyItems_returnsFailed() {
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "order-e", "ATLANTA", OrderSource.WEB, null, null, null, BigDecimal.ZERO
        );

        CompletableFuture<OrderResult> future = orderService.placeOrder(cmd);

        assertTrue(future.isCompletedExceptionally());
        ExecutionException ex = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
    }

    @Test
    void testGuestUser_usesDefaultCustomerName() throws Exception {
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "order-f", "ATLANTA", OrderSource.WEB, null,
            List.of(new OrderLineItem(Item.QDC_A101, BigDecimal.valueOf(100), "Guest")),
            null, BigDecimal.valueOf(100)
        );

        OrderResult result = orderService.placeOrder(cmd).get();

        assertEquals("guest", result.getOrderUp().getCustomerName());
        assertEquals(baseSize + 1, sink.received().size());
    }

    @Test
    void testKafkaPayload_hasCorrectStructure() throws Exception {
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "82124c69-a108-4ccc-9ac4-64566e389178", "ATLANTA", OrderSource.WEB, null,
            List.of(new OrderLineItem(Item.QDC_A101, BigDecimal.valueOf(135.50), "Bugs")),
            null, BigDecimal.valueOf(135.50)
        );

        orderService.placeOrder(cmd);

        String payload = sink.received().get(baseSize).getPayload();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(payload);

        assertEquals("82124c69-a108-4ccc-9ac4-64566e389178", json.get("id").asText());
        assertEquals("WEB", json.get("orderSource").asText());
        assertEquals("ATLANTA", json.get("location").asText());
        assertTrue(json.get("loyaltyMemberId").isNull());
    }
}
