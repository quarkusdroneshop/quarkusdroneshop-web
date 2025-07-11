package io.quarkusdroneshop.domain.valueobjects;

import java.math.BigDecimal;

public class OrderIn {
    private String orderId;
    private String lineItemId;
    private String itemName;
    private String customerName;
    private int quantity;
    private BigDecimal price;

    public OrderIn() {
        // デフォルトコンストラクタ（JSONバインディング用など）
    }

    public OrderIn(String orderId, String lineItemId, String itemName, String customerName, int quantity, BigDecimal price) {
        this.orderId = orderId;
        this.lineItemId = lineItemId;
        this.itemName = itemName;
        this.customerName = customerName;
        this.quantity = quantity;
        this.price = price;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}