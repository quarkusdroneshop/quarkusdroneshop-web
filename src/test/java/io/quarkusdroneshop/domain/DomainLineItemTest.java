package io.quarkusdroneshop.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainLineItemTest {

    @Test
    void testDefaultConstructor() {
        LineItem li = new LineItem();
        assertNull(li.item);
        assertNull(li.name);
    }

    @Test
    void testFullConstructor() {
        LineItem li = new LineItem(Item.QDC_A101, "Alice");
        assertEquals(Item.QDC_A101, li.item);
        assertEquals("Alice", li.name);
    }

    @Test
    void testToString() {
        LineItem li = new LineItem(Item.QDC_A101, "Alice");
        String s = li.toString();
        assertTrue(s.contains("Alice"));
    }

    @Test
    void testEquals() {
        LineItem a = new LineItem(Item.QDC_A101, "Alice");
        LineItem b = new LineItem(Item.QDC_A101, "Alice");
        LineItem c = new LineItem(Item.QDC_A105_Pro01, "Alice");
        LineItem d = new LineItem(Item.QDC_A101, "Bob");

        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, d);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
        assertEquals(a.hashCode(), b.hashCode());
    }
}
