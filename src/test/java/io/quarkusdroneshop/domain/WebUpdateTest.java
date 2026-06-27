package io.quarkusdroneshop.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebUpdateTest {

    // ── OrderReadyUpdate ─────────────────────────────────────────────────────

    @Test
    void testOrderReadyUpdateDefaultConstructor() {
        OrderReadyUpdate u = new OrderReadyUpdate();
        assertNull(u.orderId);
        assertNull(u.madeBy);
    }

    @Test
    void testOrderReadyUpdateFullConstructor() {
        OrderReadyUpdate u = new OrderReadyUpdate(
                "ord-1", "item-1", "Alice", Item.QDC_A101, OrderStatus.READY, "Bob");
        assertEquals("ord-1", u.orderId);
        assertEquals("item-1", u.itemId);
        assertEquals("Alice", u.name);
        assertEquals(Item.QDC_A101, u.item);
        assertEquals(OrderStatus.READY, u.status);
        assertEquals("Bob", u.madeBy);
    }

    @Test
    void testOrderReadyUpdateFromOrderUpEvent() {
        OrderUpEvent ev = new OrderUpEvent(
                EventType.QDCA10_ORDER_UP, "ord-2", "Dave", Item.QDC_A105_Pro01, "item-2", "Chef");
        OrderReadyUpdate u = new OrderReadyUpdate(ev);
        assertEquals("ord-2", u.orderId);
        assertEquals("item-2", u.itemId);
        assertEquals("Dave", u.name);
        assertEquals(Item.QDC_A105_Pro01, u.item);
        assertEquals("Chef", u.madeBy);
        assertEquals(OrderStatus.READY, u.status);
    }

    @Test
    void testOrderReadyUpdateFromOrderInEvent() {
        OrderInEvent ev = new OrderInEvent(
                EventType.QDCA10_ORDER_IN, "ord-3", "Eve", Item.QDC_A101);
        OrderReadyUpdate u = new OrderReadyUpdate(ev);
        assertEquals("ord-3", u.orderId);
        assertEquals("Eve", u.name);
        assertEquals(Item.QDC_A101, u.item);
        assertEquals(OrderStatus.READY, u.status);
    }

    @Test
    void testOrderReadyUpdateToString() {
        OrderReadyUpdate u = new OrderReadyUpdate(
                "ord-1", "item-1", "Alice", Item.QDC_A101, OrderStatus.READY, "Bob");
        String s = u.toString();
        assertTrue(s.contains("ord-1"));
        assertTrue(s.contains("Alice"));
    }

    // ── InQueueUpdate ────────────────────────────────────────────────────────

    @Test
    void testInQueueUpdateDefaultConstructor() {
        InQueueUpdate u = new InQueueUpdate();
        assertNull(u.orderId);
    }

    @Test
    void testInQueueUpdateFullConstructor() {
        InQueueUpdate u = new InQueueUpdate(
                "ord-4", "item-4", "Frank", Item.QDC_A101, OrderStatus.IN_QUEUE);
        assertEquals("ord-4", u.orderId);
        assertEquals(OrderStatus.IN_QUEUE, u.status);
    }

    @Test
    void testInQueueUpdateFromLineItemEvent() {
        OrderInEvent ev = new OrderInEvent(
                EventType.QDCA10_ORDER_IN, "ord-5", "Grace", Item.QDC_A101);
        InQueueUpdate u = new InQueueUpdate(ev);
        assertEquals("ord-5", u.orderId);
        assertEquals(OrderStatus.IN_QUEUE, u.status);
        assertEquals("Grace", u.name);
        assertEquals(Item.QDC_A101, u.item);
    }
}
