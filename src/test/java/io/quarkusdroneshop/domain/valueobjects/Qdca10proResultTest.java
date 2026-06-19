package io.quarkusdroneshop.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class Qdca10proResultTest {

    @Test
    void testWithReward() {
        OrderUp orderUp = new OrderUp("o1", "l1", "item", "proUser", 1000L);
        RewardEvent reward = new RewardEvent("proUser", "o1", BigDecimal.valueOf(20));
        Qdca10proResult result = new Qdca10proResult(orderUp, reward, false);

        assertEquals(orderUp, result.getOrderUp());
        assertEquals(reward, result.getRewardEvent());
        assertFalse(result.isEightySixed());
    }

    @Test
    void testEightySixed() {
        Qdca10proResult result = new Qdca10proResult(null, null, true);
        assertNull(result.getOrderUp());
        assertNull(result.getRewardEvent());
        assertTrue(result.isEightySixed());
    }
}
