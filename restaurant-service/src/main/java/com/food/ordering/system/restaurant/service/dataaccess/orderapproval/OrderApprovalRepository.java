package com.food.ordering.system.restaurant.service.dataaccess.orderapproval;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class OrderApprovalRepository extends RouteBuilder {
  @Override
  public void configure() {

    // returns/receives OrderApproval
    //from("direct:saveOrderApproval").routeId("OrderApprovalRepositoryRoute")
     //       .log("")
      //      .end();

  }
}
