package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.domain.valueobjects.LoyaltyUpdate;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

import java.time.Duration;

@RegisterForReflection
@Path("/dashboard")
public class LoyaltyDashboardResource {

    private static final Duration HEARTBEAT_INTERVAL = Duration.ofSeconds(15);

    Logger logger = Logger.getLogger(LoyaltyDashboardResource.class);

    @Inject
    @Channel("loyalty-updates")
    @Broadcast
    Publisher<LoyaltyUpdate> updater;

    @GET
    @Path("/loyaltystream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    public void dashboardStream(@Context SseEventSink sink, @Context Sse sse) {
        Multi<OutboundSseEvent> data = Multi.createFrom().publisher(FlowAdapters.toFlowPublisher(updater))
                .onItem().transform(item -> sse.newEventBuilder()
                        .mediaType(MediaType.APPLICATION_JSON_TYPE)
                        .data(item)
                        .build());

        Multi<OutboundSseEvent> heartbeat = Multi.createFrom().ticks().every(HEARTBEAT_INTERVAL)
                .onItem().transform(tick -> sse.newEventBuilder().comment("ping").build());

        // The data and heartbeat streams emit from different threads (the reactive-messaging
        // channel vs. the interval timer), but the underlying Vert.x HTTP response is only
        // safe to write from its own event-loop context, so route every send back onto it.
        io.vertx.core.Context vertxContext = io.vertx.core.Vertx.currentContext();
        Multi.createBy().merging().streams(data, heartbeat)
                .subscribe().with(
                        evt -> runOnContext(vertxContext, () -> sink.send(evt)),
                        err -> runOnContext(vertxContext, sink::close),
                        () -> runOnContext(vertxContext, sink::close));
    }

    private static void runOnContext(io.vertx.core.Context context, Runnable action) {
        if (context != null) {
            context.runOnContext(v -> action.run());
        } else {
            action.run();
        }
    }
}
