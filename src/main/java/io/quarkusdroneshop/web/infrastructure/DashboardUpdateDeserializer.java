package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkusdroneshop.web.domain.DashboardUpdate;

public class DashboardUpdateDeserializer extends ObjectMapperDeserializer<DashboardUpdate> {

    public DashboardUpdateDeserializer() {
        super(DashboardUpdate.class);
    }
}