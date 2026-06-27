package io.quarkusdroneshop.web.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public class RewardEvent {
    public String customerName;
    public String orderId;
    public BigDecimal rewardAmount;

    public RewardEvent() {} // JSON Deserialization 用

    public RewardEvent(String customerName, String orderId, BigDecimal rewardAmount) {
        this.customerName = customerName;
        this.orderId = orderId;
        this.rewardAmount = rewardAmount;
    }
}
