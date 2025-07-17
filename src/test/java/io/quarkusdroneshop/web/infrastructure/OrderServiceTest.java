package io.quarkusdroneshop.web.infrastructure;

import io.quarkusdroneshop.web.infrastructure.OrderService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.domain.Item;
import io.quarkusdroneshop.domain.OrderLineItem;
import io.quarkusdroneshop.domain.OrderSource;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import io.quarkusdroneshop.web.infrastructure.testsupport.KafkaTestResource;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.json.JsonReader;
import javax.ws.rs.sse.InboundSseEvent;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.json.Json;
import javax.json.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class OrderServiceTest {

    public OrderServiceTest() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceTest.class);

    private static final String expectedPayload =
        "{"
        + "\"id\":\"82124c69-a108-4ccc-9ac4-64566e389178\","
        + "\"orderSource\":\"WEB\","
        + "\"location\":\"ATLANTA\","
        + "\"loyaltyMemberId\":null,"
        + "\"qdca10LineItems\":[{"
        + "\"item\":\"QDC_A101\","
        + "\"price\":135.50,"
        + "\"name\":\"Bugs\""
        + "}],"
        + "\"qdca10proLineItems\":[]"
        + "}";

    @Inject
    @Any
    InMemoryConnector connector;

    @Inject
    OrderService orderService;

    @Test
    public void testOrderServicerOrderIn() throws Exception {
        InMemorySink<String> sink = connector.sink("orders-up");

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                "82124c69-a108-4ccc-9ac4-64566e389178",
                "ATLANTA",
                OrderSource.WEB,
                null,
                Arrays.asList(new OrderLineItem(Item.QDC_A101, BigDecimal.valueOf(135.50), "Bugs")),
                null,
                BigDecimal.valueOf(135.50)
        );

        orderService.placeOrder(placeOrderCommand);

        assertEquals(1, sink.received().size(), "1 message should be sent");

        String receivedPayload = sink.received().get(0).getPayload();
        LOGGER.info("payload received: {}", receivedPayload);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedJson = mapper.readTree(expectedPayload);
        JsonNode actualJson = mapper.readTree(receivedPayload);

        assertEquals(expectedJson, actualJson, "payload JSON should match");
    }

    private static Consumer<InboundSseEvent> onEvent = (inboundSseEvent) -> {
        String data = inboundSseEvent.readData();
        LOGGER.info("event received: {}", data);
    };
}
