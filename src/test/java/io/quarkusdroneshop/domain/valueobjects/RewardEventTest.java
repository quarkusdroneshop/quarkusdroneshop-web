package io.quarkusdroneshop.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RewardEventTest {

    @Test
    void testDefaultConstructor() {
        RewardEvent re = new RewardEvent();
        assertNull(re.getCustomerName());
        assertNull(re.getOrderId());
        assertNull(re.getPoints());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        RewardEvent re = new RewardEvent("Alice", "order-1", BigDecimal.valueOf(15.5));
        assertEquals("Alice", re.getCustomerName());
        assertEquals("order-1", re.getOrderId());
        assertEquals(BigDecimal.valueOf(15.5), re.getPoints());
    }

    @Test
    void testSetters() {
        RewardEvent re = new RewardEvent();
        re.setCustomerName("Bob");
        re.setOrderId("order-2");
        re.setPoints(BigDecimal.TEN);

        assertEquals("Bob", re.getCustomerName());
        assertEquals("order-2", re.getOrderId());
        assertEquals(BigDecimal.TEN, re.getPoints());
    }

    @Test
    void testToString() {
        RewardEvent re = new RewardEvent("Carol", "order-3", BigDecimal.ONE);
        String s = re.toString();
        assertTrue(s.contains("Carol"));
        assertTrue(s.contains("order-3"));
    }
}
