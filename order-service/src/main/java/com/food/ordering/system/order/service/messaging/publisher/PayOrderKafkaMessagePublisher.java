package com.food.ordering.system.order.service.messaging.publisher;

import com.food.ordering.system.order.service.domain.application.mapper.RestaurantMessagingDataMapper;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class PayOrderKafkaMessagePublisher extends RouteBuilder {
  @Override
  public void configure() {

    from("direct:restaurantMessagingProducerHandler").routeId("RestaurantMessagingProducerHandler")
            .bean(RestaurantMessagingDataMapper.class, "orderPaidEventToRestaurantApprovalRequestAvroModel")
            .log("${body}");

  }
}
