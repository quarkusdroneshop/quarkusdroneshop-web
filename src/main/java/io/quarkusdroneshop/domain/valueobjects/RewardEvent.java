package io.quarkusdroneshop.domain.valueobjects;

import java.math.BigDecimal;

public class RewardEvent {
    private String customerName;
    private String orderId;
    private BigDecimal points;

    public RewardEvent() {
        // デフォルトコンストラクタ（JSON用）
    }

    public RewardEvent(String customerName, String orderId, BigDecimal points) {
        this.customerName = customerName;
        this.orderId = orderId;
        this.points = points;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "RewardEvent{" +
                "customerName='" + customerName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", points=" + points +
                '}';
    }
}