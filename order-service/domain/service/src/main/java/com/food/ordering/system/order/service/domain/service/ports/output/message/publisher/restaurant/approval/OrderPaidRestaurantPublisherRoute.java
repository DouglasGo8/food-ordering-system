package com.food.ordering.system.order.service.domain.service.ports.output.message.publisher.restaurant.approval;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderPaidRestaurantPublisherRoute extends RouteBuilder {
  @Override
  public void configure() {
    // direct?seda?publish<OrderPaidEvent> ?? send to OrderEvenMapper.publishEvent
  }
}
