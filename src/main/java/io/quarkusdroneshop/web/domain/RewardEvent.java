package io.quarkusdroneshop.web.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

@RegisterForReflection
public class RewardEvent {
    public String name;
    public String orderId;
    public BigDecimal points;

    public RewardEvent() {} // JSON Deserialization 用

    public RewardEvent(String name, String orderId, BigDecimal points) {
        this.name = name;
        this.orderId = orderId;
        this.points = points;
    }
}