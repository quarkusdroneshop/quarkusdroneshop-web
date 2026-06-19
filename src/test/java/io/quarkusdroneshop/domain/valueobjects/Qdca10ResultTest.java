package io.quarkusdroneshop.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class Qdca10ResultTest {

    @Test
    void testWithReward() {
        OrderUp orderUp = new OrderUp("o1", "l1", "item", "Alice", 1000L);
        RewardEvent reward = new RewardEvent("Alice", "o1", BigDecimal.valueOf(10));
        Qdca10Result result = new Qdca10Result(orderUp, reward, false);

        assertEquals(orderUp, result.getOrderUp());
        assertEquals(reward, result.getRewardEvent());
        assertFalse(result.isEightySixed());
    }

    @Test
    void testWithoutReward() {
        OrderUp orderUp = new OrderUp("o2", "l2", "item", "Bob", 2000L);
        Qdca10Result result = new Qdca10Result(orderUp, null, true);

        assertEquals(orderUp, result.getOrderUp());
        assertNull(result.getRewardEvent());
        assertTrue(result.isEightySixed());
    }
}
