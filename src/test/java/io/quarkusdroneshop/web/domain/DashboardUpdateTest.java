package io.quarkusdroneshop.web.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkusdroneshop.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DashboardUpdateTest {

    @Test
    void testConstructorAndFields() {
        String orderId = UUID.randomUUID().toString();
        String itemId = UUID.randomUUID().toString();
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
        String orderId = UUID.randomUUID().toString();
        String itemId = UUID.randomUUID().toString();
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
    void testNonUuidOrderIdDoesNotThrow() throws Exception {
        // counter 側の orderId は自由形式の String であり UUID とは限らない
        // (このセッションで実際に非UUID文字列が原因で web-updates チャンネルが
        // クラッシュループする不具合があったため、型を String に修正した経緯を回帰させる)。
        String json = "{\"orderId\":\"aggregator-test-0001\",\"itemId\":\"item-1\",\"name\":\"Bob\","
            + "\"item\":\"QDC_A102\",\"status\":\"FULFILLED\",\"madeBy\":\"human\"}";
        ObjectMapper mapper = new ObjectMapper();
        DashboardUpdate du = mapper.readValue(json, DashboardUpdate.class);

        assertEquals("aggregator-test-0001", du.orderId);
        assertEquals("item-1", du.itemId);
    }

    @Test
    void testNullMadeBy() {
        DashboardUpdate du = new DashboardUpdate(
            UUID.randomUUID().toString(), UUID.randomUUID().toString(), "X", "Y", OrderStatus.PLACED, null);
        assertNull(du.madeBy);
    }
}
