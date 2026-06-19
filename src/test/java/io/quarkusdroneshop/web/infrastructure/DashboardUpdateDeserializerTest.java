package io.quarkusdroneshop.web.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DashboardUpdateDeserializerTest {

    @Test
    void testInstantiation() {
        DashboardUpdateDeserializer deserializer = new DashboardUpdateDeserializer();
        assertNotNull(deserializer);
    }
}
