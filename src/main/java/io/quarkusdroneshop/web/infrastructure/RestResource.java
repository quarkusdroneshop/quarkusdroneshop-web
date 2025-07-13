package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import io.quarkusdroneshop.domain.valueobjects.OrderResult;
import io.quarkusdroneshop.domain.Reward;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Map;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.concurrent.CompletionStage;

@RegisterForReflection
@Path("/")
public class RestResource {

    Logger logger = LoggerFactory.getLogger(RestResource.class);

    @ConfigProperty(name="streamUrl")
    String streamUrl;

    @ConfigProperty(name="loyaltyStreamUrl")
    String loyaltyStreamUrl;

    @ConfigProperty(name="storeId")
    String storeId;

    @ConfigProperty(name="rewardUrl")
    String rewardUrl;

    @Inject
    OrderService orderService;

    @Inject
    Reward reward;

    @Inject
    Template shopTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex(){

        return shopTemplate
                .data("streamUrl", streamUrl)
                .data("loyaltyStreamUrl", loyaltyStreamUrl)
                .data("rewardUrl", rewardUrl)
                .data("storeId", storeId)
                .data("reward", reward);
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