package io.quarkusdroneshop.web.infrastructure;

import io.quarkusdroneshop.domain.*;
import io.quarkusdroneshop.web.domain.DashboardUpdate;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import org.jboss.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

@RegisterForReflection
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ApiResource {

    Logger logger = Logger.getLogger(ApiResource.class);

    @GET
    @Path("/update")
    public Response getCreateOrderCommandJson() {
        DashboardUpdate dashboardUpdate = new DashboardUpdate(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Jeremy",
                Item.QDC_A101,
                OrderStatus.IN_QUEUE,
                null);
        return Response.ok().entity(dashboardUpdate).build();
    }

    @GET
    @Path("/placeOrderCommand")
    public Response getPlaceOrderCommand() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                UUID.randomUUID().toString(),
                "ATLANTA",
                OrderSource.WEB,
                null,
                Collections.singletonList(new OrderLineItem(Item.QDC_A102, BigDecimal.valueOf(155.50), "Goofy")),
                Collections.singletonList(new OrderLineItem(Item.QDC_A103, BigDecimal.valueOf(144.00), "Goofy")),
                BigDecimal.valueOf(5.98)
        );

        return Response.ok().entity(placeOrderCommand).build();
    }
}