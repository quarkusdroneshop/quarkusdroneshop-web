package io.quarkusdroneshop.web;

import org.junit.jupiter.api.Test;

public class WebTestItemE2EWrapperIT {

  @Test
  void qdca101() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA101E2ETest.main(new String[]{}));
  }

  @Test
  void qdca102() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA102E2ETest.main(new String[]{}));
  }

  @Test
  void qdca103() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA103E2ETest.main(new String[]{}));
  }

  @Test
  void qdca104_ac() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA104ACE2ETest.main(new String[]{}));
  }

  @Test
  void qdca104_at() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA104ATE2ETest.main(new String[]{}));
  }

  @Test
  void qdca105_pro1() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA105Pro1E2ETest.main(new String[]{}));
  }

  @Test
  void qdca105_pro2() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA105Pro2E2ETest.main(new String[]{}));
  }

  @Test
  void qdca105_pro3() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA105Pro3E2ETest.main(new String[]{}));
  }

  @Test
  void qdca105_pro4() {
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
    WebTestQDCA105Pro4E2ETest.main(new String[]{}));
  }
}