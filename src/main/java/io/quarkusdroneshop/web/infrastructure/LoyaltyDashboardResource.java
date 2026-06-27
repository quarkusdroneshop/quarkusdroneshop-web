package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.domain.valueobjects.LoyaltyUpdate;
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
public class LoyaltyDashboardResource {

    Logger logger = Logger.getLogger(LoyaltyDashboardResource.class);

    @Inject
    @Channel("loyalty-updates")
    @Broadcast
    Publisher<LoyaltyUpdate> updater;

    @GET
    @Path("/loyaltystream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    @RestStreamElementType("text/plain")
    public Publisher<LoyaltyUpdate> dashboardStream() {

        return updater;
    }
}
