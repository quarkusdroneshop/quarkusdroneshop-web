package io.quarkusdroneshop.web.infrastructure;

import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;
import io.quarkusdroneshop.web.domain.Customer360;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Customer360Deserializer implements Deserializer<Customer360> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Customer360Deserializer.class);

    private final AvroKafkaDeserializer<GenericRecord> avroDeserializer = new AvroKafkaDeserializer<>();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        avroDeserializer.configure(configs, isKey);
    }

    @Override
    public Customer360 deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            GenericRecord record = avroDeserializer.deserialize(topic, new RecordHeaders(), data);
            if (record == null) {
                return null;
            }

            String customerName = asString(record.get("customerName"));
            String loyaltyMemberId = asString(record.get("loyaltyMemberId"));
            String lastLocation = asString(record.get("lastLocation"));
            String lastOrderId = asString(record.get("lastOrderId"));
            Long lastOrderAt = asLong(record.get("lastOrderAt"));
            Long totalOrders = asLong(record.get("totalOrders"));
            Long updatedAt = asLong(record.get("updatedAt"));

            return new Customer360(customerName, loyaltyMemberId, lastLocation,
                    lastOrderId, lastOrderAt, totalOrders, updatedAt);
        } catch (Exception e) {
            LOGGER.warn("Failed to deserialize Customer360 record", e);
            return null;
        }
    }

    private static String asString(Object value) {
        return value == null ? null : value.toString();
    }

    private static Long asLong(Object value) {
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    @Override
    public void close() {
        avroDeserializer.close();
    }
}
