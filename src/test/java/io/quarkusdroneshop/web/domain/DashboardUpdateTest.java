package io.quarkusdroneshop.web.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkusdroneshop.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DashboardUpdateTest {

    @Test
    void testConstructorAndFields() {
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        DashboardUpdate du = new DashboardUpdate(orderId, itemId, "Alice", "QDC_A101", OrderStatus.IN_QUEUE, "bot");

        assertEquals(orderId, du.orderId);
        assertEquals(itemId, du.itemId);
        assertEquals("Alice", du.name);
        assertEquals("QDC_A101", du.item);
        assertEquals(OrderStatus.IN_QUEUE, du.status);
        assertEquals("bot", du.madeBy);
    }

    @Test
    void testJsonDeserialization() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        String json = String.format(
            "{\"orderId\":\"%s\",\"itemId\":\"%s\",\"name\":\"Bob\",\"item\":\"QDC_A102\",\"status\":\"FULFILLED\",\"madeBy\":\"human\"}",
            orderId, itemId
        );
        ObjectMapper mapper = new ObjectMapper();
        DashboardUpdate du = mapper.readValue(json, DashboardUpdate.class);

        assertEquals(orderId, du.orderId);
        assertEquals(itemId, du.itemId);
        assertEquals("Bob", du.name);
        assertEquals("QDC_A102", du.item);
        assertEquals(OrderStatus.FULFILLED, du.status);
        assertEquals("human", du.madeBy);
    }

    @Test
    void testNullMadeBy() {
        DashboardUpdate du = new DashboardUpdate(UUID.randomUUID(), UUID.randomUUID(), "X", "Y", OrderStatus.PLACED, null);
        assertNull(du.madeBy);
    }
}
