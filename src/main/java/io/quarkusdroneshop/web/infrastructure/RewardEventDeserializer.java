package io.quarkusdroneshop.web.infrastructure;

import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import io.quarkusdroneshop.domain.Reward;

public class RewardEventDeserializer implements Deserializer<Reward> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public Reward deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, Reward.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize Reward", e);
        }
    }

    @Override
    public void close() {}
}