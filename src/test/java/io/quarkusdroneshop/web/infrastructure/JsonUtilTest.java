package io.quarkusdroneshop.web.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilTest {

    @Test
    void testToJsonSuccess() {
        String result = JsonUtil.toJson(new SimpleBean("hello", 42));
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("42"));
    }

    @Test
    void testToJsonError() {
        String result = JsonUtil.toJson(new UnserializableBean());
        assertTrue(result.contains("error"));
    }

    static class SimpleBean {
        @JsonProperty
        public String name;
        @JsonProperty
        public int value;

        SimpleBean(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    static class UnserializableBean {
        @JsonProperty
        public String getValue() {
            throw new RuntimeException("forced serialization failure");
        }
    }
}
