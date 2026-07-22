package io.quarkusdroneshop.web.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkusdroneshop.domain.OrderStatus;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DashboardUpdate {

    // counter 側 (発行元) の orderId/itemId は自由形式の String (UUID とは限らない。
    // このセッションのテストで実際に "aggregator-test-0001" のような非UUID文字列が
    // 送られ、UUID 型だと JSON デシリアライズに失敗して web-updates チャンネル全体が
    // 恒久的にブロックされクラッシュループする不具合が発生した)。counter 側の
    // DashboardUpdate と同じく String で受ける。
    public final String orderId;

    public final String itemId;

    public final String name;

    public final String item;

    public final OrderStatus status;

    public final String madeBy;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DashboardUpdate(
            @JsonProperty("orderId") final String orderId,
            @JsonProperty("itemId") String itemId,
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
