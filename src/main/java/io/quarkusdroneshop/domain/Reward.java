package io.quarkusdroneshop.domain;

import javax.enterprise.context.ApplicationScoped;
import com.fasterxml.jackson.annotation.JsonProperty;

@ApplicationScoped
public class Reward {

    private String customerName;
    private String orderId;

    @JsonProperty("rewardAmount")
    private double rewardAmount;

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

    public double getPoints() {
        return rewardAmount;
    }

    public void setPoints(double rewardAmount) {
        this.rewardAmount = rewardAmount;
    }
}