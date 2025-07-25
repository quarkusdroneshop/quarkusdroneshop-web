package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.domain.Item;
import io.quarkusdroneshop.domain.OrderStatus;
import io.quarkusdroneshop.web.domain.DashboardUpdate;
import io.quarkusdroneshop.web.infrastructure.testsupport.KafkaTestResource;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.net.URI;
import java.util.UUID;
import java.net.URISyntaxException;
import java.util.function.Consumer;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class DashboardResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardResourceTest.class);

    private static final String expectedPayload = "data: {\"orderId\":\"82124c69-a108-4ccc-9ac4-64566e389178\",\"itemId\":\"f84cb5e2-a3fd-43af-8df8-b5d74b133115\",\"name\":\"Scotty\",\"item\":\"DRONE_WITH_ROOM\",\"status\":\"IN_QUEUE\",\"madeBy\":\"\"}";

    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    public void testStreaming() {

        DashboardUpdate dashboardUpdate = new DashboardUpdate(
                UUID.fromString("82124c69-a108-4ccc-9ac4-64566e389178"),
                UUID.fromString("f84cb5e2-a3fd-43af-8df8-b5d74b133115"),
                "Scotty",
                "QDC_A101",
                OrderStatus.IN_QUEUE,
                null
        );

        LOGGER.info("{}", dashboardUpdate);

        URI uri = null;
        try {
            uri = new URI("http://localhost:8080/dashboard/stream");
        } catch (URISyntaxException e) {
            assertNull(e);
        }
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(uri);

        SseEventSource sseEventSource = SseEventSource.target(target).build();
        sseEventSource.register(onEvent, onError, onComplete);
        sseEventSource.open();

        // marshall the JSON payload
        String formattedUpdate = JsonUtil.toJson(dashboardUpdate);

        // send the JSON payload
        InMemorySource<String> source = connector.source("web-updates");
        source.send(formattedUpdate);
        LOGGER.info("updates sent {}", formattedUpdate);
    }

    // verify that the event matches our expectations
    private static Consumer<InboundSseEvent> onEvent = (inboundSseEvent) -> {
        String data = inboundSseEvent.readData();
        LOGGER.info("event received: {}", data);
        assertEquals(expectedPayload, data);
    };

    private static Consumer<Throwable> onError = (throwable) -> {
        LOGGER.error("error {}", throwable.getMessage());
        assertNull(throwable);
    };

    private static Runnable onComplete = () -> LOGGER.info("done!");
}
