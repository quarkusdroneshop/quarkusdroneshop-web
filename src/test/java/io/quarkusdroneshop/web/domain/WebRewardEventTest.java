package io.quarkusdroneshop.web.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WebRewardEventTest {

    @Test
    void testDefaultConstructor() {
        RewardEvent re = new RewardEvent();
        assertNull(re.customerName);
        assertNull(re.orderId);
        assertNull(re.rewardAmount);
    }

    @Test
    void testAllArgsConstructor() {
        RewardEvent re = new RewardEvent("Alice", "order-1", BigDecimal.valueOf(50.0));
        assertEquals("Alice", re.customerName);
        assertEquals("order-1", re.orderId);
        assertEquals(BigDecimal.valueOf(50.0), re.rewardAmount);
    }
}
