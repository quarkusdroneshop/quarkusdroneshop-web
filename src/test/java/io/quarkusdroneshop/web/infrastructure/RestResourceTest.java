package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkusdroneshop.domain.valueobjects.OrderUp;
import io.quarkusdroneshop.domain.valueobjects.Qdca10Result;
import io.quarkusdroneshop.domain.valueobjects.Qdca10proResult;
import io.quarkusdroneshop.domain.valueobjects.RewardEvent;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class RestResourceTest {

    @InjectMock
    OrderService orderService;

    private static final String ORDER_PAYLOAD = "{"
        + "\"id\":\"test-order-1\","
        + "\"storeId\":\"ATLANTA\","
        + "\"orderSource\":\"WEB\","
        + "\"rewardsId\":\"guest\","
        + "\"qdca10Items\":[{\"item\":\"QDC_A101\",\"price\":135.50,\"name\":\"Alice\"}],"
        + "\"qdca10proItems\":[],"
        + "\"total\":135.50"
        + "}";

    @Test
    void testGetIndexReturnsHtml() {
        given()
            .get("/")
            .then()
            .statusCode(200)
            .contentType(containsString("text/html"));
    }

    @Test
    void testOrderInReturns202() {
        OrderUp orderUp = new OrderUp("test-order-1", "line-1", "QDC_A101", "guest", System.currentTimeMillis());
        when(orderService.placeOrder(any(PlaceOrderCommand.class)))
            .thenReturn(CompletableFuture.completedFuture(new Qdca10Result(orderUp, null, false)));

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(ORDER_PAYLOAD)
            .post("/order")
            .then()
            .statusCode(202)
            .body("order.orderId", equalTo("test-order-1"));
    }

    @Test
    void testOrderInWithRewardReturns202() {
        OrderUp orderUp = new OrderUp("test-order-2", "line-2", "QDC_A101", "guest", System.currentTimeMillis());
        RewardEvent reward = new RewardEvent("guest", "test-order-2", BigDecimal.valueOf(20.0));
        when(orderService.placeOrder(any(PlaceOrderCommand.class)))
            .thenReturn(CompletableFuture.completedFuture(new Qdca10Result(orderUp, reward, false)));

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(ORDER_PAYLOAD)
            .post("/order")
            .then()
            .statusCode(202)
            .body("order.orderId", equalTo("test-order-2"))
            .body("reward", notNullValue());
    }

    @Test
    void testOrderInProCustomerReturns202() {
        OrderUp orderUp = new OrderUp("test-order-3", "line-3", "QDC_A105_Pro01", "proUser", System.currentTimeMillis());
        when(orderService.placeOrder(any(PlaceOrderCommand.class)))
            .thenReturn(CompletableFuture.completedFuture(new Qdca10proResult(orderUp, null, false)));

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(ORDER_PAYLOAD)
            .post("/order")
            .then()
            .statusCode(202);
    }

    @Test
    void testOrderInReturns500OnError() {
        when(orderService.placeOrder(any(PlaceOrderCommand.class)))
            .thenReturn(CompletableFuture.failedFuture(new RuntimeException("order processing failed")));

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(ORDER_PAYLOAD)
            .post("/order")
            .then()
            .statusCode(500);
    }
}
