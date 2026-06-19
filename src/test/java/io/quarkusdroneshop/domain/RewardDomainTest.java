package io.quarkusdroneshop.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RewardDomainTest {

    @Test
    void testGettersAndSetters() {
        Reward reward = new Reward();
        assertNull(reward.getCustomerName());
        assertNull(reward.getOrderId());
        assertEquals(0.0, reward.getPoints());

        reward.setCustomerName("Alice");
        reward.setOrderId("order-1");
        reward.setPoints(99.5);

        assertEquals("Alice", reward.getCustomerName());
        assertEquals("order-1", reward.getOrderId());
        assertEquals(99.5, reward.getPoints());
    }
}
