package io.quarkusdroneshop.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemTest {

    @Test
    void testDefaultConstructor() {
        OrderLineItem o = new OrderLineItem();
        assertNull(o.getItem());
        assertNull(o.getPrice());
        assertNull(o.getName());
    }

    @Test
    void testFullConstructor() {
        OrderLineItem o = new OrderLineItem(Item.QDC_A101, new BigDecimal("3.50"), "Alice");
        assertEquals(Item.QDC_A101, o.getItem());
        assertEquals(new BigDecimal("3.50"), o.getPrice());
        assertEquals("Alice", o.getName());
    }

    @Test
    void testSetters() {
        OrderLineItem o = new OrderLineItem();
        o.setItem(Item.QDC_A105_Pro01);
        o.setPrice(new BigDecimal("5.00"));
        o.setName("Bob");
        assertEquals(Item.QDC_A105_Pro01, o.getItem());
        assertEquals(new BigDecimal("5.00"), o.getPrice());
        assertEquals("Bob", o.getName());
    }

    @Test
    void testToString() {
        OrderLineItem o = new OrderLineItem(Item.QDC_A101, new BigDecimal("3.50"), "Alice");
        String s = o.toString();
        assertTrue(s.contains("Alice"));
        assertTrue(s.contains("3.50"));
    }

    @Test
    void testEquals() {
        OrderLineItem a = new OrderLineItem(Item.QDC_A101, new BigDecimal("3.50"), "Alice");
        OrderLineItem b = new OrderLineItem(Item.QDC_A101, new BigDecimal("3.50"), "Alice");
        OrderLineItem c = new OrderLineItem(Item.QDC_A105_Pro01, new BigDecimal("3.50"), "Alice");
        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "str");
        assertEquals(a.hashCode(), b.hashCode());
    }
}
