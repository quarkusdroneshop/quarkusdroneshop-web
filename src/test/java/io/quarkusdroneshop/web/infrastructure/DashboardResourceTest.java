package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.domain.OrderStatus;
import io.quarkusdroneshop.web.domain.DashboardUpdate;
import io.quarkusdroneshop.web.domain.RewardEvent;
import io.quarkusdroneshop.web.infrastructure.testsupport.KafkaTestResource;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.sse.SseEventSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
class DashboardResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardResourceTest.class);

    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    void testDashboardStreamEndpointExists() throws Exception {
        AtomicBoolean connected = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/dashboard/stream");
        try (SseEventSource source = SseEventSource.target(target).reconnectingEvery(1, TimeUnit.SECONDS).build()) {
            source.register(event -> latch.countDown(), err -> {}, () -> {});
            source.open();
            connected.set(source.isOpen());
            latch.await(2, TimeUnit.SECONDS);
        } finally {
            client.close();
        }

        assertTrue(connected.get(), "SSE connection to /dashboard/stream should open successfully");
    }

    @Test
    void testRewardsStreamEndpointExists() throws Exception {
        AtomicBoolean connected = new AtomicBoolean(false);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/dashboard/rewards/stream");
        try (SseEventSource source = SseEventSource.target(target).reconnectingEvery(1, TimeUnit.SECONDS).build()) {
            source.register(event -> {}, err -> {}, () -> {});
            source.open();
            connected.set(source.isOpen());
            Thread.sleep(500);
        } finally {
            client.close();
        }

        assertTrue(connected.get(), "SSE connection to /dashboard/rewards/stream should open successfully");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testDashboardUpdateIsPublished() {
        DashboardUpdate update = new DashboardUpdate(
            UUID.fromString("82124c69-a108-4ccc-9ac4-64566e389178"),
            UUID.fromString("f84cb5e2-a3fd-43af-8df8-b5d74b133115"),
            "Alice",
            "QDC_A101",
            OrderStatus.IN_QUEUE,
            null
        );

        InMemorySource<DashboardUpdate> source = (InMemorySource<DashboardUpdate>)(InMemorySource<?>)connector.source("web-updates");
        source.send(update);

        LOGGER.info("DashboardUpdate sent: {}", update.orderId);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testRewardEventIsPublished() {
        RewardEvent reward = new RewardEvent("Bob", "order-99", BigDecimal.valueOf(30.0));

        InMemorySource<RewardEvent> source = (InMemorySource<RewardEvent>)(InMemorySource<?>)connector.source("rewards");
        source.send(reward);

        LOGGER.info("RewardEvent sent for customer: {}", reward.customerName);
    }
}
