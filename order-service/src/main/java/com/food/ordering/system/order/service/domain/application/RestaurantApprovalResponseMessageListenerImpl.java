package com.food.ordering.system.order.service.domain.application;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantApprovalResponseMessageListenerImpl extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    //from("direct:orderApproved")
    //from("direct:orderRejected")
  }
}
