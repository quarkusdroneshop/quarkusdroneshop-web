package io.quarkusdroneshop.web.infrastructure.testsupport;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import io.smallrye.reactive.messaging.memory.InMemoryConnector;

import java.util.HashMap;
import java.util.Map;

public class KafkaTestResource  implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();
        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("web-updates"));
        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("loyalty-updates"));
        env.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("rewards"));
        env.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("orders-up"));
        return env;
    }

    @Override
    public void stop() {

        InMemoryConnector.clear();
    }
}
