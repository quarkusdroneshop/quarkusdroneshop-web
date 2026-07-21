package io.quarkusdroneshop.web.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Customer360 {
    public String customerName;
    public String loyaltyMemberId;
    public String lastLocation;
    public String lastOrderId;
    public Long lastOrderAt;
    public Long totalOrders;
    public Long updatedAt;

    public Customer360() {}

    public Customer360(String customerName, String loyaltyMemberId, String lastLocation,
                        String lastOrderId, Long lastOrderAt, Long totalOrders, Long updatedAt) {
        this.customerName = customerName;
        this.loyaltyMemberId = loyaltyMemberId;
        this.lastLocation = lastLocation;
        this.lastOrderId = lastOrderId;
        this.lastOrderAt = lastOrderAt;
        this.totalOrders = totalOrders;
        this.updatedAt = updatedAt;
    }
}
