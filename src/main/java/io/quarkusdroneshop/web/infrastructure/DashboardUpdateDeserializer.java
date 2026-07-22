package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkusdroneshop.web.domain.DashboardUpdate;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RegisterForReflection
public class DashboardUpdateDeserializer extends ObjectMapperDeserializer<DashboardUpdate> {

    private static final Logger logger = LoggerFactory.getLogger(DashboardUpdateDeserializer.class);

    public DashboardUpdateDeserializer() {
        super(DashboardUpdate.class);
    }

    // web-updates は 1件のパース不能レコードが永久に消費をブロックしうる
    // (SmallRye は失敗レコードを自動でスキップしない)。1件の不正データで
    // web-updates チャンネル全体・ひいてはアプリ自体がクラッシュループしないよう、
    // パース失敗はログのみで読み飛ばす (このリポジトリの他の Deserializer と同じ方針)。
    @Override
    public DashboardUpdate deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (Exception e) {
            logger.warn("Skipping unparseable DashboardUpdate on topic {}", topic, e);
            return null;
        }
    }
}
