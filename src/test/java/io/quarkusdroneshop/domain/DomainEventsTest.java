package io.quarkusdroneshop.domain;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DomainEventsTest {

    // ── EventType ────────────────────────────────────────────────────────────

    @Test
    void testEventTypeValues() {
        EventType[] values = EventType.values();
        assertTrue(values.length > 0);
        assertEquals(EventType.ORDER_PLACED, EventType.valueOf("ORDER_PLACED"));
        assertEquals(EventType.EIGHTY_SIX, EventType.valueOf("EIGHTY_SIX"));
        assertEquals(EventType.QDCA10_ORDER_IN, EventType.valueOf("QDCA10_ORDER_IN"));
        assertEquals(EventType.QDCA10_ORDER_UP, EventType.valueOf("QDCA10_ORDER_UP"));
    }

    // ── OrderUpEvent ─────────────────────────────────────────────────────────

    @Test
    void testOrderUpEventDefaultConstructor() {
        OrderUpEvent e = new OrderUpEvent();
        assertNull(e.madeBy);
    }

    @Test
    void testOrderUpEventFullConstructor() {
        OrderUpEvent e = new OrderUpEvent(
                EventType.QDCA10_ORDER_UP, "ord-1", "Alice", Item.QDC_A101, "item-1", "Chef");
        assertEquals("ord-1", e.orderId);
        assertEquals("Alice", e.name);
        assertEquals(Item.QDC_A101, e.item);
        assertEquals("item-1", e.itemId);
        assertEquals("Chef", e.madeBy);
        assertEquals(EventType.QDCA10_ORDER_UP, e.getEventType());
    }

    @Test
    void testOrderUpEventToString() {
        OrderUpEvent e = new OrderUpEvent(
                EventType.QDCA10_ORDER_UP, "ord-2", "Bob", Item.QDC_A105_Pro01, "item-2", "Staff");
        String s = e.toString();
        assertTrue(s.contains("ord-2"));
        assertTrue(s.contains("Staff"));
    }

    @Test
    void testOrderUpEventEquals() {
        // equals/hashCode is based on madeBy only (see OrderUpEvent implementation)
        OrderUpEvent a = new OrderUpEvent(
                EventType.QDCA10_ORDER_UP, "ord-3", "Carol", Item.QDC_A101, "item-3", "ChefA");
        OrderUpEvent b = new OrderUpEvent(
                EventType.QDCA10_ORDER_UP, "ord-3", "Carol", Item.QDC_A101, "item-3", "ChefA");
        OrderUpEvent c = new OrderUpEvent(
                EventType.QDCA10_ORDER_UP, "ord-9", "Carol", Item.QDC_A101, "item-3", "ChefB");
        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "str");
        assertEquals(a.hashCode(), b.hashCode());
    }

    // ── OrderInEvent ─────────────────────────────────────────────────────────

    @Test
    void testOrderInEventDefaultConstructor() {
        OrderInEvent e = new OrderInEvent();
        assertNull(e.orderId);
    }

    @Test
    void testOrderInEventConstructors() {
        OrderInEvent a = new OrderInEvent(
                EventType.QDCA10_ORDER_IN, "ord-4", "Dave", Item.QDC_A101);
        assertEquals("ord-4", a.orderId);
        assertEquals("Dave", a.name);
        assertEquals(EventType.QDCA10_ORDER_IN, a.getEventType());

        OrderInEvent b = new OrderInEvent(
                EventType.QDCA10PRO_ORDER_IN, "ord-5", "item-5", "Eve", Item.QDC_A105_Pro01);
        assertEquals("ord-5", b.orderId);
        assertEquals("Eve", b.name);
        assertEquals(EventType.QDCA10PRO_ORDER_IN, b.getEventType());
    }

    // ── EightySixEvent ───────────────────────────────────────────────────────

    @Test
    void testEightySixEventDefaultConstructor() {
        EightySixEvent e = new EightySixEvent();
        assertEquals(EventType.EIGHTY_SIX, e.getEventType());
    }

    @Test
    void testEightySixEventWithItem() {
        EightySixEvent e = new EightySixEvent(Item.QDC_A101);
        assertEquals(EventType.EIGHTY_SIX, e.getEventType());
    }

    // ── EightySixException ───────────────────────────────────────────────────

    @Test
    void testEightySixException() {
        EightySixException ex = new EightySixException(Item.QDC_A105_Pro01);
        assertEquals(Item.QDC_A105_Pro01, ex.getItem());
        Collection<EightySixEvent> events = ex.getEvents();
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(EventType.EIGHTY_SIX, events.iterator().next().getEventType());
    }

    // ── LineItemEvent (via subclass) ─────────────────────────────────────────

    @Test
    void testLineItemEventConstructorWithEventType() {
        OrderUpEvent e = new OrderUpEvent();
        e.eventType = EventType.QDCA10_ORDER_UP;
        assertEquals(EventType.QDCA10_ORDER_UP, e.getEventType());
    }
}
