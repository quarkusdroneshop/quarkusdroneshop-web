package io.quarkusdroneshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
public class OrderPlacedEvent {

    public String id;

    OrderSource orderSource;

    public String rewardsId;

    public List<LineItem> qdca10 = new ArrayList<>();

    public List<LineItem> qdca10pro = new ArrayList<>();

    public final EventType eventType = EventType.ORDER_PLACED;

    public OrderPlacedEvent() {
    }

    public List<LineItem> getqdca10() {
        return qdca10 == null ? new ArrayList<>() : qdca10;
    }

    public List<LineItem> getqdca10pro() {
        return qdca10pro == null ? new ArrayList<>() : qdca10pro;
    }

    public void addqdca10(final String id, final List<LineItem> beverageList) {
        this.id = id;
        this.qdca10.addAll(beverageList);
    }

    public void addqdca10proItems(final String id, final List<LineItem> qdca10proList) {
        this.id = id;
        this.qdca10pro.addAll(qdca10proList);
    }

    public OrderPlacedEvent(String id, OrderSource orderSource, String rewardsId, List<LineItem> qdca10, List<LineItem> qdca10pro) {
        this.id = id;
        this.orderSource = orderSource;
        this.rewardsId = rewardsId;
        this.qdca10 = qdca10;
        this.qdca10pro = qdca10pro;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderPlacedEvent.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("orderSource=" + orderSource)
                .add("rewardsId=" + rewardsId)
                .add("qdca10=" + qdca10)
                .add("qdca10pro=" + qdca10pro)
                .add("eventType=" + eventType)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPlacedEvent that = (OrderPlacedEvent) o;
        return Objects.equals(id, that.id) &&
                orderSource == that.orderSource &&
                rewardsId == that.rewardsId &&
                Objects.equals(qdca10, that.qdca10) &&
                Objects.equals(qdca10pro, that.qdca10pro) &&
                eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderSource, rewardsId, qdca10, qdca10pro, eventType);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderSource getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(OrderSource orderSource) {
        this.orderSource = orderSource;
    }

    public String getRewardsId() {
        return rewardsId;
    }

    public void setRewardsId(String rewardsId) { this.rewardsId = rewardsId; }

    public void setqdca10(List<LineItem> qdca10) {
        this.qdca10 = qdca10;
    }

    public void setqdca10pro(List<LineItem> qdca10pro) {
        this.qdca10pro = qdca10pro;
    }
}
