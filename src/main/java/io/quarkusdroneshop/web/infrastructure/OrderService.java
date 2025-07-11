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
import java.util.Objects;
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
        
        // 1件目の qdca10 または qdca10pro を取得
        Optional<OrderLineItem> firstItem = placeOrderCommand.getqdca10Items()
            .flatMap(items -> items.stream().findFirst());

        if (firstItem.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("No order items found"));
        }

        OrderLineItem item = firstItem.get();
        long now = Instant.now().toEpochMilli();

        String orderId = placeOrderCommand.getId();
        String lineItemId = "lineitem-" + now;
        String itemName = item.getName();
        String customerName = placeOrderCommand.getRewardsId().orElse("guest");

        // OrderUp 作成
        OrderUp orderUp = new OrderUp(
            orderId,
            lineItemId,
            itemName,
            customerName,
            now
        );

        // RewardEvent 判定
        RewardEvent rewardEvent = null;
        int quantity = 1;  // ← OrderLineItemにgetQuantity()がなければ仮値
        if (quantity >= 5) {
            BigDecimal points = item.getPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .multiply(BigDecimal.valueOf(0.15));
            rewardEvent = new RewardEvent(customerName, orderId, points);
            orderUp.setRewardPoints(points);
        }

        // Kafka送信
        WebOrderCommand webOrderCommand = new WebOrderCommand(placeOrderCommand);
        ordersOutEmitter.send(toJson(webOrderCommand));

        // 結果を返す（pro 判定）
        if (customerName.startsWith("pro")) {
            return CompletableFuture.completedFuture(new Qdca10proResult(orderUp, rewardEvent, false));
        } else {
            return CompletableFuture.completedFuture(new Qdca10Result(orderUp, rewardEvent, false));
        }
    }
}