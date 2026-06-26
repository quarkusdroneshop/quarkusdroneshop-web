package io.quarkusdroneshop.web.infrastructure;

import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import io.quarkusdroneshop.web.domain.RewardEvent;

public class RewardEventDeserializer implements Deserializer<RewardEvent> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public RewardEvent deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, RewardEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize RewardEvent", e);
        }
    }

    @Override
    public void close() {}
}