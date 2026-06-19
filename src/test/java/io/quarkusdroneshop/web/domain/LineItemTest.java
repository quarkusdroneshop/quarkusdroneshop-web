package io.quarkusdroneshop.web.domain;

import io.quarkusdroneshop.domain.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LineItemTest {

    @Test
    void testConstructorAndGetters() {
        LineItem li = new LineItem(Item.QDC_A101, "Drone A", "order-1", BigDecimal.valueOf(135.50));
        assertEquals(Item.QDC_A101, li.getItem());
        assertEquals("Drone A", li.getName());
        assertEquals("order-1", li.getOrderId());
    }

    @Test
    void testSetters() {
        LineItem li = new LineItem(Item.QDC_A101, "old", "old-order", BigDecimal.ONE);
        li.setItem(Item.QDC_A102);
        li.setName("new name");
        li.setOrderId("new-order");

        assertEquals(Item.QDC_A102, li.getItem());
        assertEquals("new name", li.getName());
        assertEquals("new-order", li.getOrderId());
    }

    @Test
    void testToString() {
        LineItem li = new LineItem(Item.QDC_A103, "item", "o1", BigDecimal.TEN);
        String s = li.toString();
        assertTrue(s.contains("QDC_A103"));
        assertTrue(s.contains("item"));
    }

    @Test
    void testEquals() {
        LineItem a = new LineItem(Item.QDC_A101, "name", "order-1", BigDecimal.ONE);
        LineItem b = new LineItem(Item.QDC_A101, "name", "order-1", BigDecimal.TEN);
        LineItem c = new LineItem(Item.QDC_A102, "name", "order-1", BigDecimal.ONE);

        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void testHashCode() {
        LineItem a = new LineItem(Item.QDC_A101, "name", "order-1", BigDecimal.ONE);
        LineItem b = new LineItem(Item.QDC_A101, "name", "order-1", BigDecimal.TEN);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testNullItem() {
        LineItem a = new LineItem(null, "name", "order-1", BigDecimal.ONE);
        LineItem b = new LineItem(null, "name", "order-1", BigDecimal.ONE);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        LineItem c = new LineItem(Item.QDC_A101, "name", "order-1", BigDecimal.ONE);
        assertNotEquals(a, c);
    }
}
