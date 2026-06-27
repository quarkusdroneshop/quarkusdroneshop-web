package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.qute.Template;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import io.quarkusdroneshop.domain.Reward;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.Map;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.concurrent.CompletionStage;

@RegisterForReflection
@Path("/")
public class RestResource {

    Logger logger = LoggerFactory.getLogger(RestResource.class);

    @ConfigProperty(name = "streamUrl")
    String streamUrl;

    @ConfigProperty(name = "loyaltyStreamUrl")
    String loyaltyStreamUrl;

    @ConfigProperty(name = "storeId")
    String storeId;

    @ConfigProperty(name = "rewardUrl")
    String rewardUrl;

    @Inject
    OrderService orderService;

    @Inject
    Reward reward;

    @Inject
    Template shopTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        return shopTemplate
                .data("streamUrl", streamUrl)
                .data("loyaltyStreamUrl", loyaltyStreamUrl)
                .data("rewardUrl", rewardUrl)
                .data("storeId", storeId)
                .data("reward", reward)
                .render();
    }

    @POST
    @Path("/order")
    public CompletionStage<Response> orderIn(final PlaceOrderCommand placeOrderCommand) {
    
        logger.debug("order received: {}", placeOrderCommand.toString());
    
        return orderService.placeOrder(placeOrderCommand)
            .thenApply(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("order", result.getOrderUp());
    
                if (result.getRewardEvent() != null) {
                    responseBody.put("reward", result.getRewardEvent());
                }
    
                return Response.accepted().entity(responseBody).build();
            })
            .exceptionally(ex -> {
                logger.error("Order failed", ex);
                return Response.serverError().entity(ex.getMessage()).build();
            });
    }
}
