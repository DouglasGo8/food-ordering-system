package com.food.ordering.system.restaurant.service.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;


@ApplicationScoped
@NoArgsConstructor
public class RestaurantApprovalRequestMessageListener extends RouteBuilder {
  @Override
  public void configure() {

    // RestaurantApprovalRequestMessageListenerImpl Implementation Sct::07::Vdo58
    // receives RestaurantApprovalRequest
    from("direct:approveOrder").routeId("RestaurantApprovalRequestMessageListenerRouteId")
            .to("direct:persistOrderApproval")
            // RestaurantApprovalRequestHelper.persistOrderApproval(RestaurantApprovalRequest) // returns OrderApprovalEvent
            // to(seda:orderApprovalMessage)
            .end();
  }
}
