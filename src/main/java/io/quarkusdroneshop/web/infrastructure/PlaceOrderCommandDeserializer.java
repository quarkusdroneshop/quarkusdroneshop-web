package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;

public class PlaceOrderCommandDeserializer extends ObjectMapperDeserializer<PlaceOrderCommand> {
    public PlaceOrderCommandDeserializer() {
        super(PlaceOrderCommand.class);
    }
}