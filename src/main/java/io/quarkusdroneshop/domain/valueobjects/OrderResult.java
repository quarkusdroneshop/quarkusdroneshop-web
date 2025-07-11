package io.quarkusdroneshop.domain.valueobjects;

public interface OrderResult {
    boolean isEightySixed();
    OrderUp getOrderUp();
    RewardEvent getRewardEvent();
}