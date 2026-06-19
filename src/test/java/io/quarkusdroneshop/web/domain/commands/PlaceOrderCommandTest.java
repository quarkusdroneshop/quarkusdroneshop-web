package io.quarkusdroneshop.web.domain.commands;

import io.quarkusdroneshop.domain.CommandType;
import io.quarkusdroneshop.domain.Item;
import io.quarkusdroneshop.domain.OrderLineItem;
import io.quarkusdroneshop.domain.OrderSource;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderCommandTest {

    @Test
    void testDefaultConstructor() {
        PlaceOrderCommand cmd = new PlaceOrderCommand();
        assertEquals(CommandType.PLACE_ORDER, cmd.getCommandType());
        assertNull(cmd.getId());
        assertNull(cmd.getStoreId());
        assertFalse(cmd.getRewardsId().isPresent());
        assertFalse(cmd.getqdca10Items().isPresent());
        assertFalse(cmd.getqdca10proItems().isPresent());
    }

    @Test
    void testAllArgsConstructor() {
        List<OrderLineItem> items = List.of(new OrderLineItem(Item.QDC_A101, BigDecimal.valueOf(100), "Alice"));
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "id-1", "ATLANTA", OrderSource.WEB, "member-1", items, null, BigDecimal.valueOf(100)
        );

        assertEquals("id-1", cmd.getId());
        assertEquals("ATLANTA", cmd.getStoreId());
        assertEquals(OrderSource.WEB, cmd.getOrderSource());
        assertTrue(cmd.getRewardsId().isPresent());
        assertEquals("member-1", cmd.getRewardsId().get());
        assertTrue(cmd.getqdca10Items().isPresent());
        assertFalse(cmd.getqdca10proItems().isPresent());
        assertEquals(BigDecimal.valueOf(100), cmd.getTotal());
    }

    @Test
    void testSetters() {
        PlaceOrderCommand cmd = new PlaceOrderCommand();
        cmd.setStoreId("RALEIGH");
        cmd.setOrderSource(OrderSource.COUNTER);
        cmd.setRewardsId("rewards-id");
        cmd.setTotal(BigDecimal.valueOf(200));
        cmd.setqdca10Items(List.of());
        cmd.setqdca10proItems(List.of());

        assertEquals("RALEIGH", cmd.getStoreId());
        assertEquals(OrderSource.COUNTER, cmd.getOrderSource());
        assertEquals("rewards-id", cmd.getRewardsId().get());
        assertEquals(BigDecimal.valueOf(200), cmd.getTotal());
    }

    @Test
    void testToString() {
        PlaceOrderCommand cmd = new PlaceOrderCommand(
            "id-1", "ATLANTA", OrderSource.WEB, "member-1", null, null, BigDecimal.ZERO
        );
        String s = cmd.toString();
        assertTrue(s.contains("id-1"));
        assertTrue(s.contains("ATLANTA"));
    }

    @Test
    void testEqualsAndHashCode() {
        PlaceOrderCommand a = new PlaceOrderCommand("id", "ATLANTA", OrderSource.WEB, "m1", null, null, BigDecimal.ZERO);
        PlaceOrderCommand b = new PlaceOrderCommand("id", "ATLANTA", OrderSource.WEB, "m1", null, null, BigDecimal.ZERO);
        PlaceOrderCommand c = new PlaceOrderCommand("other", "ATLANTA", OrderSource.WEB, "m1", null, null, BigDecimal.ZERO);

        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
        assertEquals(a.hashCode(), b.hashCode());
    }
}
