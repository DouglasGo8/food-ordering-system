package com.food.ordering.system.restaurant.service.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;


@ApplicationScoped
@NoArgsConstructor
public class RestaurantApprovalRequestMessageListener  extends RouteBuilder {
  @Override
  public void configure() throws Exception {


    // receives RestaurantApprovalRequest
    //from("direct:approveOrder").routeId("RestaurantApprovalRequestMessageListenerRoute")
    //.log("test")
    // RestaurantApprovalRequestHelper.persistOrderApproval(RestaurantApprovalRequest) // returns OrderApprovalEvent
    // to(seda:orderApprovalMessage)
    //        .end();
  }
}
