package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @Inject
    OrderService orderService;

    @Inject
    Template shopTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex(){

        return shopTemplate
                .data("streamUrl", streamUrl)
                .data("loyaltyStreamUrl", loyaltyStreamUrl)
                .data("storeId", storeId);
    }

    @POST
    @Path("order")
    public CompletionStage<Response> orderIn(final PlaceOrderCommand placeOrderCommand) {

        logger.debug("order received: {}", placeOrderCommand.toString());

        return orderService.placeOrder(placeOrderCommand)
            .thenApply(res -> Response.accepted().entity(placeOrderCommand).build()).exceptionally(ex -> {
                    logger.error(ex.getMessage());
                    return Response.serverError().entity(ex).build();
            });
    }

}
