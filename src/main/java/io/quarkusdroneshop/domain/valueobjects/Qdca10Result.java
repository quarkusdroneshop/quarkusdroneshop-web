package io.quarkusdroneshop.domain.valueobjects;

public class Qdca10Result implements OrderResult {

    private final OrderUp orderUp;
    private final RewardEvent rewardEvent;
    private final boolean isEightySixed;

    public Qdca10Result(OrderUp orderUp, RewardEvent rewardEvent, boolean isEightySixed) {
        this.orderUp = orderUp;
        this.rewardEvent = rewardEvent;
        this.isEightySixed = isEightySixed;
    }

    @Override
    public boolean isEightySixed() {
        return isEightySixed;
    }

    @Override
    public OrderUp getOrderUp() {
        return orderUp;
    }

    @Override
    public RewardEvent getRewardEvent() {
        return rewardEvent;
    }
}