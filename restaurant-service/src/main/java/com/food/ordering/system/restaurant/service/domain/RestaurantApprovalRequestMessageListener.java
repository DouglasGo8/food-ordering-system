package com.food.ordering.system.restaurant.service.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantApprovalRequestMessageListener extends RouteBuilder {

  @Override
  public void configure() {

    // receives RestaurantApprovalRequest
    //from("direct:approveOrder").routeId("RestaurantApprovalRequestMessageListenerRoute")
            //.log("test")
            // RestaurantApprovalRequestHelper.persistOrderApproval(RestaurantApprovalRequest) // returns OrderApprovalEvent
            // seda:kafka
    //        .end();
  }
}
