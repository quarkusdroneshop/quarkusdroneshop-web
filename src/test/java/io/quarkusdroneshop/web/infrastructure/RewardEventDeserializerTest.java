package io.quarkusdroneshop.web.infrastructure;

import io.quarkusdroneshop.domain.Reward;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class RewardEventDeserializerTest {

    @Test
    void testDeserializeValid() {
        RewardEventDeserializer d = new RewardEventDeserializer();
        byte[] json = "{\"customerName\":\"Alice\",\"orderId\":\"o1\",\"rewardAmount\":10.5}".getBytes(StandardCharsets.UTF_8);
        Reward r = d.deserialize("test-topic", json);
        assertEquals("Alice", r.getCustomerName());
        assertEquals("o1", r.getOrderId());
        assertEquals(10.5, r.getPoints());
    }

    @Test
    void testDeserializeInvalid() {
        RewardEventDeserializer d = new RewardEventDeserializer();
        byte[] bad = "not-json".getBytes(StandardCharsets.UTF_8);
        assertThrows(RuntimeException.class, () -> d.deserialize("test-topic", bad));
    }

    @Test
    void testConfigureAndClose() {
        RewardEventDeserializer d = new RewardEventDeserializer();
        assertDoesNotThrow(() -> d.configure(null, false));
        assertDoesNotThrow(d::close);
    }
}
