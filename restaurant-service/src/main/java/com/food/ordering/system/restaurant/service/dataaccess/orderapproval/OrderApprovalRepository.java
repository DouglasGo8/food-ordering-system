package com.food.ordering.system.restaurant.service.dataaccess.orderapproval;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import java.util.UUID;

@NoArgsConstructor
@ApplicationScoped
public class OrderApprovalRepository extends RouteBuilder {
  @Override
  public void configure() {

    // returns/receives OrderApproval
    from("direct:saveOrderApproval").routeId("OrderApprovalRepositoryRouteId")
            // before save should save on tbl_approval/ cast from OrderApproval to Table
            .transform(simple("${variable.restaurant.orderApproval}"))
            .setHeader("id", simple(UUID.randomUUID().toString()))
            //.log("UUID: ${body.restaurantId.value}")
            .to("sql-stored:classpath:templates/insertOrderApproval.sql")
            .log(LoggingLevel.INFO, "OrderApproval for id: ${header.id} Saved")
            .end();

  }
}
