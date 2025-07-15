package io.quarkusdroneshop.web.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkusdroneshop.domain.*;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.UUID;

@RegisterForReflection
public class DashboardUpdate {

    public final UUID orderId;

    public final UUID itemId;

    public final String name;

    public final String item;

    public final OrderStatus status;

    public final String madeBy;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DashboardUpdate(
            @JsonProperty("orderId") final UUID orderId,
            @JsonProperty("itemId") UUID itemId,
            @JsonProperty("name") String name,
            @JsonProperty("item") String item,
            @JsonProperty("status") OrderStatus status,
            @JsonProperty("madeBy") String madeBy) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.name = name;
        this.item = item;
        this.status = status;
        this.madeBy = madeBy;
    }
}
