package com.food.ordering.system.order.service.application.mediation.messaging;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantMessagingProducerHandler extends RouteBuilder {
  @Override
  public void configure() {

    from("direct:restaurantMessagingProducerHandler").routeId("RestaurantMessagingProducerHandler")
            .bean(RestaurantMessagingDataMapper.class)
            .log("${body}");

  }
}
