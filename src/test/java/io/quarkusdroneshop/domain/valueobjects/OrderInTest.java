package io.quarkusdroneshop.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderInTest {

    @Test
    void testDefaultConstructor() {
        OrderIn o = new OrderIn();
        assertNull(o.getOrderId());
    }

    @Test
    void testFullConstructor() {
        OrderIn o = new OrderIn("ord-1", "li-1", "Coffee", "Alice", 2, new BigDecimal("3.50"));
        assertEquals("ord-1", o.getOrderId());
        assertEquals("li-1", o.getLineItemId());
        assertEquals("Coffee", o.getItemName());
        assertEquals("Alice", o.getCustomerName());
        assertEquals(2, o.getQuantity());
        assertEquals(new BigDecimal("3.50"), o.getPrice());
    }

    @Test
    void testSetters() {
        OrderIn o = new OrderIn();
        o.setOrderId("ord-2");
        o.setLineItemId("li-2");
        o.setItemName("Tea");
        o.setCustomerName("Bob");
        o.setQuantity(3);
        o.setPrice(new BigDecimal("2.00"));
        assertEquals("ord-2", o.getOrderId());
        assertEquals("li-2", o.getLineItemId());
        assertEquals("Tea", o.getItemName());
        assertEquals("Bob", o.getCustomerName());
        assertEquals(3, o.getQuantity());
        assertEquals(new BigDecimal("2.00"), o.getPrice());
    }
}
