package io.quarkusdroneshop.web.domain.commands;

import io.quarkusdroneshop.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class WebOrderCommand {

    private final String id;

    private final String orderSource = "WEB";

    private final String location;

    private final String loyaltyMemberId;

    private final List<OrderLineItem> qdca10LineItems;

    private final List<OrderLineItem> qdca10proLineItems;

    public WebOrderCommand(final PlaceOrderCommand placeOrderCommand) {

        this.id = placeOrderCommand.getId();
        this.location = placeOrderCommand.getStoreId();

        if (placeOrderCommand.getqdca10Items().isPresent()) {
            this.qdca10LineItems = placeOrderCommand.getqdca10Items().get();
        }else{
            this.qdca10LineItems = new ArrayList<>(0);
        }

        if (placeOrderCommand.getqdca10proItems().isPresent()) {
            this.qdca10proLineItems = placeOrderCommand.getqdca10proItems().get();
        }else {
            this.qdca10proLineItems = new ArrayList<>(0);
        }

        if (placeOrderCommand.getRewardsId().isPresent()) {
            this.loyaltyMemberId = placeOrderCommand.getRewardsId().get();
        }else{
            this.loyaltyMemberId = null;
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebOrderCommand.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("orderSource='" + orderSource + "'")
                .add("location='" + location + "'")
                .add("loyaltyMemberId='" + loyaltyMemberId + "'")
                .add("qdca10LineItems=" + qdca10LineItems)
                .add("qdca10proLineItems=" + qdca10proLineItems)
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebOrderCommand that = (WebOrderCommand) o;

        if (!Objects.equals(id, that.id)) return false;
        if (orderSource != null ? !orderSource.equals(that.orderSource) : that.orderSource != null) return false;
        if (!Objects.equals(location, that.location)) return false;
        if (!Objects.equals(loyaltyMemberId, that.loyaltyMemberId))
            return false;
        if (!Objects.equals(qdca10LineItems, that.qdca10LineItems))
            return false;
        return Objects.equals(qdca10proLineItems, that.qdca10proLineItems);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderSource != null ? orderSource.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (loyaltyMemberId != null ? loyaltyMemberId.hashCode() : 0);
        result = 31 * result + (qdca10LineItems != null ? qdca10LineItems.hashCode() : 0);
        result = 31 * result + (qdca10proLineItems != null ? qdca10proLineItems.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public String getLocation() {
        return location;
    }

    public String getLoyaltyMemberId() {
        return loyaltyMemberId;
    }

    public List<OrderLineItem> getqdca10LineItems() {
        return qdca10LineItems;
    }

    public List<OrderLineItem> getqdca10proLineItems() {
        return qdca10proLineItems;
    }
}
