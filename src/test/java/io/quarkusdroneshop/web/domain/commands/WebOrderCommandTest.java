package io.quarkusdroneshop.web.domain.commands;

import io.quarkusdroneshop.domain.Item;
import io.quarkusdroneshop.domain.OrderLineItem;
import io.quarkusdroneshop.domain.OrderSource;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WebOrderCommandTest {

    @Test
    void testFromPlaceOrderCommandWithAllFields() {
        List<OrderLineItem> a10Items = List.of(new OrderLineItem(Item.QDC_A101, BigDecimal.valueOf(100), "Alice"));
        List<OrderLineItem> proItems = List.of(new OrderLineItem(Item.QDC_A105_Pro01, BigDecimal.valueOf(500), "Alice"));
        PlaceOrderCommand cmd = new PlaceOrderCommand("id-1", "ATLANTA", OrderSource.WEB, "member-1", a10Items, proItems, BigDecimal.valueOf(600));

        WebOrderCommand woc = new WebOrderCommand(cmd);

        assertEquals("id-1", woc.getId());
        assertEquals("WEB", woc.getOrderSource());
        assertEquals("ATLANTA", woc.getLocation());
        assertEquals("member-1", woc.getLoyaltyMemberId());
        assertEquals(1, woc.getqdca10LineItems().size());
        assertEquals(1, woc.getqdca10proLineItems().size());
    }

    @Test
    void testFromPlaceOrderCommandWithNullItems() {
        PlaceOrderCommand cmd = new PlaceOrderCommand("id-2", "RALEIGH", OrderSource.COUNTER, null, null, null, BigDecimal.ZERO);

        WebOrderCommand woc = new WebOrderCommand(cmd);

        assertNull(woc.getLoyaltyMemberId());
        assertTrue(woc.getqdca10LineItems().isEmpty());
        assertTrue(woc.getqdca10proLineItems().isEmpty());
    }

    @Test
    void testToString() {
        PlaceOrderCommand cmd = new PlaceOrderCommand("id-1", "ATLANTA", OrderSource.WEB, "m1", null, null, BigDecimal.ZERO);
        WebOrderCommand woc = new WebOrderCommand(cmd);
        String s = woc.toString();
        assertTrue(s.contains("id-1"));
        assertTrue(s.contains("ATLANTA"));
        assertTrue(s.contains("WEB"));
    }

    @Test
    void testEqualsAndHashCode() {
        PlaceOrderCommand cmd = new PlaceOrderCommand("id-1", "ATLANTA", OrderSource.WEB, "m1", null, null, BigDecimal.ZERO);
        WebOrderCommand a = new WebOrderCommand(cmd);
        WebOrderCommand b = new WebOrderCommand(cmd);

        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testNotEquals() {
        PlaceOrderCommand cmd1 = new PlaceOrderCommand("id-1", "ATLANTA", OrderSource.WEB, "m1", null, null, BigDecimal.ZERO);
        PlaceOrderCommand cmd2 = new PlaceOrderCommand("id-2", "RALEIGH", OrderSource.WEB, "m2", null, null, BigDecimal.ZERO);
        WebOrderCommand a = new WebOrderCommand(cmd1);
        WebOrderCommand b = new WebOrderCommand(cmd2);

        assertNotEquals(a, b);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }
}
