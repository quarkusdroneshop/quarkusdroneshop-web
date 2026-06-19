package io.quarkusdroneshop.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoyaltyUpdateTest {

    @Test
    void testConstructorAndGetters() {
        LoyaltyUpdate lu = new LoyaltyUpdate("user@example.com", "100pts");
        assertEquals("user@example.com", lu.getEmail());
        assertEquals("100pts", lu.getReward());
    }

    @Test
    void testToString() {
        LoyaltyUpdate lu = new LoyaltyUpdate("a@b.com", "50pts");
        String s = lu.toString();
        assertTrue(s.contains("a@b.com"));
        assertTrue(s.contains("50pts"));
    }

    @Test
    void testEquals() {
        LoyaltyUpdate a = new LoyaltyUpdate("a@b.com", "50pts");
        LoyaltyUpdate b = new LoyaltyUpdate("a@b.com", "50pts");
        LoyaltyUpdate c = new LoyaltyUpdate("c@d.com", "50pts");
        LoyaltyUpdate d = new LoyaltyUpdate("a@b.com", "99pts");

        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, d);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void testHashCode() {
        LoyaltyUpdate a = new LoyaltyUpdate("a@b.com", "50pts");
        LoyaltyUpdate b = new LoyaltyUpdate("a@b.com", "50pts");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testNullFields() {
        LoyaltyUpdate lu = new LoyaltyUpdate(null, null);
        assertNull(lu.getEmail());
        assertNull(lu.getReward());
        assertEquals(lu, new LoyaltyUpdate(null, null));
        assertEquals(lu.hashCode(), new LoyaltyUpdate(null, null).hashCode());
    }
}
