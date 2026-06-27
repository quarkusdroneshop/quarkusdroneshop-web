package io.quarkusdroneshop.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderPlacedEventTest {

    private LineItem li(Item item, String name) {
        return new LineItem(item, name);
    }

    @Test
    void testDefaultConstructor() {
        OrderPlacedEvent e = new OrderPlacedEvent();
        assertNotNull(e.getqdca10());
        assertNotNull(e.getqdca10pro());
        assertEquals(EventType.ORDER_PLACED, e.eventType);
    }

    @Test
    void testFullConstructor() {
        List<LineItem> bev = List.of(li(Item.QDC_A101, "Alice"));
        List<LineItem> pro = List.of(li(Item.QDC_A105_Pro01, "Bob"));
        OrderPlacedEvent e = new OrderPlacedEvent("ord-1", OrderSource.WEB, "rwd-1", bev, pro);
        assertEquals("ord-1", e.getId());
        assertEquals(OrderSource.WEB, e.getOrderSource());
        assertEquals("rwd-1", e.getRewardsId());
        assertEquals(1, e.getqdca10().size());
        assertEquals(1, e.getqdca10pro().size());
    }

    @Test
    void testAddqdca10() {
        OrderPlacedEvent e = new OrderPlacedEvent();
        e.addqdca10("ord-2", List.of(li(Item.QDC_A101, "Carol")));
        assertEquals("ord-2", e.getId());
        assertEquals(1, e.getqdca10().size());
    }

    @Test
    void testAddqdca10proItems() {
        OrderPlacedEvent e = new OrderPlacedEvent();
        e.addqdca10proItems("ord-3", List.of(li(Item.QDC_A105_Pro01, "Dave")));
        assertEquals("ord-3", e.getId());
        assertEquals(1, e.getqdca10pro().size());
    }

    @Test
    void testGetqdca10NullSafe() {
        OrderPlacedEvent e = new OrderPlacedEvent();
        e.qdca10 = null;
        assertNotNull(e.getqdca10());
        assertTrue(e.getqdca10().isEmpty());
    }

    @Test
    void testGetqdca10proNullSafe() {
        OrderPlacedEvent e = new OrderPlacedEvent();
        e.qdca10pro = null;
        assertNotNull(e.getqdca10pro());
        assertTrue(e.getqdca10pro().isEmpty());
    }

    @Test
    void testSetters() {
        OrderPlacedEvent e = new OrderPlacedEvent();
        e.setId("id-x");
        e.setOrderSource(OrderSource.WEB);
        e.setRewardsId("rwd-x");
        List<LineItem> items = List.of(li(Item.QDC_A101, "Test"));
        e.setqdca10(items);
        e.setqdca10pro(items);
        assertEquals("id-x", e.getId());
        assertEquals(OrderSource.WEB, e.getOrderSource());
        assertEquals("rwd-x", e.getRewardsId());
        assertEquals(1, e.getqdca10().size());
        assertEquals(1, e.getqdca10pro().size());
    }

    @Test
    void testToString() {
        OrderPlacedEvent e = new OrderPlacedEvent("ord-99", OrderSource.WEB, "rwd-99",
                List.of(), List.of());
        String s = e.toString();
        assertTrue(s.contains("ord-99"));
        assertTrue(s.contains("rwd-99"));
    }

    @Test
    void testEqualsAndHashCode() {
        List<LineItem> bev = List.of(li(Item.QDC_A101, "Alice"));
        OrderPlacedEvent a = new OrderPlacedEvent("x", OrderSource.WEB, "r", bev, List.of());
        OrderPlacedEvent b = new OrderPlacedEvent("x", OrderSource.WEB, "r", bev, List.of());
        OrderPlacedEvent c = new OrderPlacedEvent("y", OrderSource.WEB, "r", bev, List.of());
        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
