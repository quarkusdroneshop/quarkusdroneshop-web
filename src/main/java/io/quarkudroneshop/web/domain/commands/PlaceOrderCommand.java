package io.quarkusdroneshop.web.domain.commands;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.domain.CommandType;
import io.quarkusdroneshop.domain.OrderLineItem;
import io.quarkusdroneshop.domain.OrderSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

@RegisterForReflection
public class PlaceOrderCommand {

    private final CommandType commandType = CommandType.PLACE_ORDER;
    List<OrderLineItem> qdca10Items;
    List<OrderLineItem> qdca10proItems;
    private String id;
    private String storeId;
    private OrderSource orderSource;
    private String rewardsId;
    private BigDecimal total;

    public PlaceOrderCommand() {
    }

    public PlaceOrderCommand(String id, String storeId, OrderSource orderSource, String rewardsId, List<OrderLineItem> qdca10Items, List<OrderLineItem> qdca10proItems, BigDecimal total) {
        this.id = id;
        this.orderSource = orderSource;
        this.storeId = storeId;
        this.rewardsId = rewardsId;
        this.qdca10Items = qdca10Items;
        this.qdca10proItems = qdca10proItems;
        this.total = total;
    }

    public Optional<String> getRewardsId() {
        return Optional.ofNullable(rewardsId);
    }

    public Optional<List<OrderLineItem>> getqdca10Items() {
        return Optional.ofNullable(qdca10Items);
    }

    public Optional<List<OrderLineItem>> getqdca10proItems() {
        return Optional.ofNullable(qdca10proItems);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlaceOrderCommand.class.getSimpleName() + "[", "]")
                .add("commandType=" + commandType)
                .add("qdca10Items=" + qdca10Items)
                .add("qdca10proItems=" + qdca10proItems)
                .add("id='" + id + "'")
                .add("storeId='" + storeId + "'")
                .add("orderSource=" + orderSource)
                .add("rewardsId='" + rewardsId + "'")
                .add("total=" + total)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOrderCommand that = (PlaceOrderCommand) o;
        return commandType == that.commandType &&
                Objects.equals(qdca10Items, that.qdca10Items) &&
                Objects.equals(qdca10proItems, that.qdca10proItems) &&
                Objects.equals(id, that.id) &&
                Objects.equals(storeId, that.storeId) &&
                orderSource == that.orderSource &&
                Objects.equals(rewardsId, that.rewardsId) &&
                Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, qdca10Items, qdca10proItems, id, storeId, orderSource, rewardsId, total);
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getId() {
        return id;
    }

    public String getStoreId() {
        return storeId;
    }

    public OrderSource getOrderSource() {
        return orderSource;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setqdca10Items(List<OrderLineItem> qdca10Items) {
        this.qdca10Items = qdca10Items;
    }

    public void setqdca10proItems(List<OrderLineItem> qdca10proItems) {
        this.qdca10proItems = qdca10proItems;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setOrderSource(OrderSource orderSource) {
        this.orderSource = orderSource;
    }

    public void setRewardsId(String rewardsId) {
        this.rewardsId = rewardsId;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
