package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.domain.valueobjects.LoyaltyUpdate;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
class LoyaltyDashboardTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoyaltyDashboardTest.class);

    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    void testLoyaltyStreamEndpointExists() throws Exception {
        AtomicBoolean connected = new AtomicBoolean(false);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/dashboard/loyaltystream");
        try (SseEventSource source = SseEventSource.target(target).reconnectingEvery(1, TimeUnit.SECONDS).build()) {
            source.register(event -> {}, err -> {}, () -> {});
            source.open();
            connected.set(source.isOpen());
            Thread.sleep(500);
        } finally {
            client.close();
        }

        assertTrue(connected.get(), "SSE connection to /dashboard/loyaltystream should open successfully");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testLoyaltyUpdateIsPublished() {
        LoyaltyUpdate update = new LoyaltyUpdate("user@example.com", "500pts");

        InMemorySource<LoyaltyUpdate> source = (InMemorySource<LoyaltyUpdate>)(InMemorySource<?>)connector.source("loyalty-updates");
        source.send(update);

        LOGGER.info("LoyaltyUpdate sent for: {}", update.getEmail());
    }
}
