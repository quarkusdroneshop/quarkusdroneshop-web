package io.quarkusdroneshop.web.infrastructure;

import io.quarkusdroneshop.web.domain.DashboardUpdate;
import io.quarkusdroneshop.web.domain.RewardEvent;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.reactivestreams.Publisher;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterForReflection
@Path("/dashboard")
public class DashboardResource {

    Logger logger = Logger.getLogger(DashboardResource.class);

    @Inject
    @Channel("web-updates")
    @Broadcast
    Publisher<DashboardUpdate> updater;

    @Inject
    @Channel("rewards")
    @Broadcast
    Publisher<RewardEvent> rewards;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    @RestStreamElementType("application/json")
    public Publisher<DashboardUpdate> dashboardStream() {
        return updater;
    }

    @GET
    @Path("/rewards/stream") 
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType("application/json")
    public Publisher<RewardEvent> streamRewards() {
        return rewards;
    }
}
