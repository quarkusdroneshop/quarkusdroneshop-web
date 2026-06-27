package io.quarkusdroneshop.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RewardTest {

    @Test
    void testGettersAndSetters() {
        Reward r = new Reward();
        r.setCustomerName("Alice");
        r.setOrderId("ord-1");
        r.setPoints(150.0);
        assertEquals("Alice", r.getCustomerName());
        assertEquals("ord-1", r.getOrderId());
        assertEquals(150.0, r.getPoints());
    }

    @Test
    void testDefaultValues() {
        Reward r = new Reward();
        assertNull(r.getCustomerName());
        assertNull(r.getOrderId());
        assertEquals(0.0, r.getPoints());
    }
}
