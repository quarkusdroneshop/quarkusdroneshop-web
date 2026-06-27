package io.quarkusdroneshop.web.domain;

import io.quarkusdroneshop.domain.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WebDomainLineItemTest {

    @Test
    void testConstructorAndGetters() {
        LineItem li = new LineItem(Item.QDC_A101, "Alice", "ord-1", new BigDecimal("3.50"));
        assertEquals(Item.QDC_A101, li.getItem());
        assertEquals("Alice", li.getName());
        assertEquals("ord-1", li.getOrderId());
    }

    @Test
    void testSetters() {
        LineItem li = new LineItem(Item.QDC_A101, "Alice", "ord-1", new BigDecimal("3.50"));
        li.setItem(Item.QDC_A105_Pro01);
        li.setName("Bob");
        li.setOrderId("ord-2");
        assertEquals(Item.QDC_A105_Pro01, li.getItem());
        assertEquals("Bob", li.getName());
        assertEquals("ord-2", li.getOrderId());
    }

    @Test
    void testToString() {
        LineItem li = new LineItem(Item.QDC_A101, "Alice", "ord-1", new BigDecimal("3.50"));
        String s = li.toString();
        assertTrue(s.contains("Alice"));
        assertTrue(s.contains("ord-1"));
    }

    @Test
    void testEquals() {
        LineItem a = new LineItem(Item.QDC_A101, "Alice", "ord-1", new BigDecimal("3.50"));
        LineItem b = new LineItem(Item.QDC_A101, "Alice", "ord-1", new BigDecimal("3.50"));
        LineItem c = new LineItem(Item.QDC_A105_Pro01, "Alice", "ord-1", new BigDecimal("3.50"));
        LineItem d = new LineItem(Item.QDC_A101, "Bob", "ord-1", new BigDecimal("3.50"));
        LineItem e = new LineItem(Item.QDC_A101, "Alice", "ord-9", new BigDecimal("3.50"));

        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, d);
        assertNotEquals(a, e);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testEqualsNullFields() {
        LineItem a = new LineItem(null, null, null, null);
        LineItem b = new LineItem(null, null, null, null);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
