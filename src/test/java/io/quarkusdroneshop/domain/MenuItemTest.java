package io.quarkusdroneshop.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void testDefaultConstructor() {
        MenuItem m = new MenuItem();
        assertNull(m.getItem());
        assertNull(m.getPrice());
    }

    @Test
    void testFullConstructor() {
        MenuItem m = new MenuItem(Item.QDC_A101, new BigDecimal("3.50"));
        assertEquals(Item.QDC_A101, m.getItem());
        assertEquals(new BigDecimal("3.50"), m.getPrice());
    }

    @Test
    void testSetters() {
        MenuItem m = new MenuItem();
        m.setItem(Item.QDC_A105_Pro01);
        m.setPrice(new BigDecimal("5.00"));
        assertEquals(Item.QDC_A105_Pro01, m.getItem());
        assertEquals(new BigDecimal("5.00"), m.getPrice());
    }

    @Test
    void testToString() {
        MenuItem m = new MenuItem(Item.QDC_A101, new BigDecimal("3.50"));
        String s = m.toString();
        assertTrue(s.contains("3.50"));
    }

    @Test
    void testEquals() {
        MenuItem a = new MenuItem(Item.QDC_A101, new BigDecimal("3.50"));
        MenuItem b = new MenuItem(Item.QDC_A101, new BigDecimal("3.50"));
        MenuItem c = new MenuItem(Item.QDC_A105_Pro01, new BigDecimal("3.50"));
        MenuItem d = new MenuItem(Item.QDC_A101, new BigDecimal("9.99"));

        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, d);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void testHashCode() {
        MenuItem a = new MenuItem(Item.QDC_A101, new BigDecimal("3.50"));
        MenuItem b = new MenuItem(Item.QDC_A101, new BigDecimal("3.50"));
        assertEquals(a.hashCode(), b.hashCode());
    }
}
