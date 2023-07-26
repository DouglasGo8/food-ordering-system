package com.food.ordering.system.order.service.messaging.producer;

import com.food.ordering.system.order.service.application.service.mapper.RestaurantMessagingDataMapper;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantMessagingProducerHandler extends RouteBuilder {
  @Override
  public void configure() {

    from("direct:restaurantMessagingProducerHandler").routeId("RestaurantMessagingProducerHandler")
            .bean(RestaurantMessagingDataMapper.class, "orderPaidEventToRestaurantApprovalRequestAvroModel")
            .log("${body}");

  }
}
