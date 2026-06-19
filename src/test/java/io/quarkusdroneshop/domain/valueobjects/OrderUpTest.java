package io.quarkusdroneshop.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderUpTest {

    @Test
    void testConstructorAndGetters() {
        OrderUp orderUp = new OrderUp("order-1", "line-1", "QDC_A101", "Alice", 1000L);

        assertEquals("order-1", orderUp.getOrderId());
        assertEquals("line-1", orderUp.getLineItemId());
        assertEquals("QDC_A101", orderUp.getItemName());
        assertEquals("Alice", orderUp.getCustomerName());
        assertEquals(1000L, orderUp.getTimestamp());
        assertNull(orderUp.getRewardPoints());
        assertNull(orderUp.getMadeBy());
    }

    @Test
    void testSetters() {
        OrderUp orderUp = new OrderUp("o", "l", "item", "customer", 0L);

        orderUp.setOrderId("new-order");
        orderUp.setLineItemId("new-line");
        orderUp.setItemName("QDC_A102");
        orderUp.setCustomerName("Bob");
        orderUp.setTimestamp(9999L);
        orderUp.setRewardPoints(BigDecimal.valueOf(15.5));
        orderUp.setMadeBy("bot");

        assertEquals("new-order", orderUp.getOrderId());
        assertEquals("new-line", orderUp.getLineItemId());
        assertEquals("QDC_A102", orderUp.getItemName());
        assertEquals("Bob", orderUp.getCustomerName());
        assertEquals(9999L, orderUp.getTimestamp());
        assertEquals(BigDecimal.valueOf(15.5), orderUp.getRewardPoints());
        assertEquals("bot", orderUp.getMadeBy());
    }
}
