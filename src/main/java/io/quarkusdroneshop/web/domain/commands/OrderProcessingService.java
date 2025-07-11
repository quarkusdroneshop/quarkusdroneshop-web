package io.quarkusdroneshop.web.domain.commands;

import io.quarkusdroneshop.domain.valueobjects.OrderResult;

import io.quarkusdroneshop.domain.valueobjects.OrderIn;
import io.quarkusdroneshop.domain.valueobjects.OrderUp;
import io.quarkusdroneshop.domain.valueobjects.RewardEvent;
import io.quarkusdroneshop.domain.valueobjects.Qdca10Result;
import io.quarkusdroneshop.domain.valueobjects.Qdca10proResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class OrderProcessingService {

    public CompletableFuture<OrderResult> placeOrder(OrderIn orderIn) {
        return CompletableFuture.supplyAsync(() -> {

            long now = Instant.now().toEpochMilli();

            // OrderUp 作成
            OrderUp orderUp = new OrderUp(
                orderIn.getOrderId(),
                orderIn.getLineItemId(),
                orderIn.getItemName(),
                orderIn.getCustomerName(),
                now
            );

            // RewardEvent（リワードポイントが5個以上なら15%付与）
            RewardEvent rewardEvent = null;
            if (orderIn.getQuantity() >= 5) {
                BigDecimal points = orderIn.getPrice()
                    .multiply(BigDecimal.valueOf(orderIn.getQuantity()))
                    .multiply(BigDecimal.valueOf(0.15));
                rewardEvent = new RewardEvent(orderIn.getCustomerName(), orderIn.getOrderId(), points);
                orderUp.setRewardPoints(points); // OrderUpにも反映
            }

            // Proかどうかで振り分け
            if (orderIn.getCustomerName().startsWith("pro")) {
                return new Qdca10proResult(orderUp, rewardEvent, false);
            } else {
                return new Qdca10Result(orderUp, rewardEvent, false);
            }
        });
    }
}