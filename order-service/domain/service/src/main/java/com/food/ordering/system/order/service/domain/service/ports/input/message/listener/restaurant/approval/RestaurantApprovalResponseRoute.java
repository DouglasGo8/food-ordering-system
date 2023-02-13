package com.food.ordering.system.order.service.domain.service.ports.input.message.listener.restaurant.approval;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantApprovalResponseRoute extends RouteBuilder {
  @Override
  public void configure() {
    // rest?direct?seda?orderApproved(RestaurantApprovalResponse body)
    // rest?direct?seda?orderRejected(RestaurantApprovalResponse body)
  }
}
