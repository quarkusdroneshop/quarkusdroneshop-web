package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkusdroneshop.web.domain.DashboardUpdate;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DashboardUpdateDeserializer extends ObjectMapperDeserializer<DashboardUpdate> {

    public DashboardUpdateDeserializer() {
        super(DashboardUpdate.class);
    }
}