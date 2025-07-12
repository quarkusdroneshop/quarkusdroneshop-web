package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.domain.valueobjects.OrderResult;
import io.quarkusdroneshop.domain.valueobjects.Qdca10Result;
import io.quarkusdroneshop.domain.valueobjects.Qdca10proResult;
import io.quarkusdroneshop.domain.valueobjects.OrderUp;
import io.quarkusdroneshop.domain.valueobjects.RewardEvent;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import io.quarkusdroneshop.web.domain.commands.WebOrderCommand;
import io.quarkusdroneshop.domain.OrderLineItem;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.Optional;

import static io.quarkusdroneshop.web.infrastructure.JsonUtil.toJson;

@RegisterForReflection
@ApplicationScoped
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    @Channel("orders-up")
    Emitter<String> ordersOutEmitter;

    public CompletableFuture<OrderResult> placeOrder(final PlaceOrderCommand placeOrderCommand) {

        logger.debug("PlaceOrderCommandReceived: {}", placeOrderCommand);
    
        List<OrderLineItem> allItems = Stream.concat(
            placeOrderCommand.getqdca10Items().orElse(List.of()).stream(),
            placeOrderCommand.getqdca10proItems().orElse(List.of()).stream()
        ).toList();
    
        if (allItems.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("No order items found"));
        }
    
        String orderId = placeOrderCommand.getId();
        String customerName = placeOrderCommand.getRewardsId().orElse("guest");
    
        OrderUp orderUp = null;
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQuantity = 0;
    
        long now = Instant.now().toEpochMilli();
    
        for (int i = 0; i < allItems.size(); i++) {
            OrderLineItem item = allItems.get(i);
            String lineItemId = "lineitem-" + now + "-" + i;
    
            orderUp = new OrderUp(
                orderId,
                lineItemId,
                item.getName(),
                customerName,
                now + i  // ユニークなタイムスタンプ（省略可）
            );
    
            totalPrice = totalPrice.add(item.getPrice());
            totalQuantity++;
        }
    
        // WebOrderCommandをKafkaに送信（元の全体情報）
        WebOrderCommand webOrderCommand = new WebOrderCommand(placeOrderCommand);
        ordersOutEmitter.send(toJson(webOrderCommand));
    
        // RewardEvent（合計数量が5以上なら発行）
        RewardEvent rewardEvent = null;
        if (totalQuantity >= 5) {
            BigDecimal rewardPoints = totalPrice
                .multiply(BigDecimal.valueOf(0.15)); // 15%還元
            rewardEvent = new RewardEvent(customerName, orderId, rewardPoints);
    
            if (orderUp != null) {
                orderUp.setRewardPoints(rewardPoints);
            }
        }
    
        // 結果を返す（pro 判定）
        if (customerName.startsWith("pro")) {
            return CompletableFuture.completedFuture(new Qdca10proResult(orderUp, rewardEvent, false));
        } else {
            return CompletableFuture.completedFuture(new Qdca10Result(orderUp, rewardEvent, false));
        }
    }
}