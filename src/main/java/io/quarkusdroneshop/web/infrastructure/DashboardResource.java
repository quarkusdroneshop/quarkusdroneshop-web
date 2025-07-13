package io.quarkusdroneshop.web.infrastructure;

import io.quarkusdroneshop.web.domain.DashboardUpdate;
import io.quarkusdroneshop.web.domain.RewardEvent;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Channel;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.Templates;

@RegisterForReflection
@Path("/dashboard")
public class DashboardResource {

    Logger logger = Logger.getLogger(DashboardResource.class);

    @Inject
    @Channel("web-updates")
    @Broadcast
    Publisher<DashboardUpdate> updater;

    @Inject
    @Channel("rewards") // Kafka topic 'rewards' に接続している SmallRye チャネル
    @Broadcast
    Publisher<RewardEvent> rewards;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    @SseElementType("text/plain") // denotes that the contained data, within this SSE, is just regular text/plain data
    public Publisher<DashboardUpdate> dashboardStream() {
        return updater;
    }

    @GET
    @Path("/rewards/stream") // ← 新しく追加。重複回避！
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("application/json") // JSONで送るなら明示的に
    public Publisher<RewardEvent> streamRewards() {
        return rewards;
    }
}
