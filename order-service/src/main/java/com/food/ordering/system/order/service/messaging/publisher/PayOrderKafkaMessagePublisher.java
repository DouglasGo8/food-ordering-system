package com.food.ordering.system.order.service.messaging.publisher;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class PayOrderKafkaMessagePublisher extends RouteBuilder {
  @Override
  public void configure() {

    //from("direct:restaurantMessagingProducerHandler").routeId("RestaurantMessagingProducerHandler")
    //        .bean(RestaurantMessagingDataMapper.class, "orderPaidEventToRestaurantApprovalRequestAvroModel")
    //        .log("${body}");

  }
}
