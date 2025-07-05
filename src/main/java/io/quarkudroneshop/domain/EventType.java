package io.quarkusdroneshop.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum EventType {
    QDCA10_ORDER_IN, QDCA10_ORDER_UP, EIGHTY_SIX, QDCA10PRO_ORDER_IN, QDCA10PRO_ORDER_UP, QDCA10PRO_PLACED, RESTOCK, NEW_ORDER, ORDER_PLACED
}
