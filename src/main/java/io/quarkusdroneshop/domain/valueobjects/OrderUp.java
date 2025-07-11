package io.quarkusdroneshop.domain.valueobjects;

import java.math.BigDecimal;

public class OrderUp {
    private String orderId;
    private String lineItemId;
    private String itemName;
    private String customerName;
    private long timestamp;
    private BigDecimal rewardPoints;

    public OrderUp(String orderId, String lineItemId, String itemName, String customerName, long timestamp) {
        this.orderId = orderId;
        this.lineItemId = lineItemId;
        this.itemName = itemName;
        this.customerName = customerName;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(BigDecimal rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}