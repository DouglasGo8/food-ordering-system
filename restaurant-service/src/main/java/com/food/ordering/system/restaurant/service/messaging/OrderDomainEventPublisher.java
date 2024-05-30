package com.food.ordering.system.restaurant.service.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class OrderDomainEventPublisher extends RouteBuilder {
  @Override
  public void configure() {

    //from("seda:orderDomainEventPublisher").routeId("OrderDomainEventPublisherRoute")
    //        .log("test")
    //        .end();

  }
}
