package io.quarkusdroneshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@RegisterForReflection
public class OrderLineItem {

    // マイクロサービスをまたぐ連携は dataproduct 経由に統一する方針のため、
    // order-events (orders-in 由来) と orders-up (QDCA10/QDCA10pro 発行) の両方が
    // 同じ itemId で明細を突合できるよう、ここ (Web) で明細IDを採番する。
    // counter はこの itemId をそのまま LineItem に引き継ぐ (自前で新規生成しない)。
    String itemId;

    Item item;

    BigDecimal price;

    String name;

    public OrderLineItem() {
        this.itemId = UUID.randomUUID().toString();
    }

    public OrderLineItem(Item item, BigDecimal price, String name) {
        this.itemId = UUID.randomUUID().toString();
        this.item = item;
        this.price = price;
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderLineItem.class.getSimpleName() + "[", "]")
                .add("itemId='" + itemId + "'")
                .add("item=" + item)
                .add("price=" + price)
                .add("name='" + name + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return item == that.item
                && Objects.equals(price, that.price)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, price, name);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
