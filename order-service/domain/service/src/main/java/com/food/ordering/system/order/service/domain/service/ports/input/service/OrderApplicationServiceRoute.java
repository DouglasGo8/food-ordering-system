package com.food.ordering.system.order.service.domain.service.ports.input.service;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderApplicationServiceRoute extends RouteBuilder {

  @Override
  public void configure() {
    // rest?direct?seda
    // createOrder(CreateOrderCommand) implicit Input Camel Binding with CreateOrderResponse returns
    // @Valid CreateOrderCommand in OrderDataMapper Bean
    // trackOrder(TrackOrderQuery trackOrderQuery) implicit Input Camel Binding with TrackOrderResponse return
    // @Valid TrackOrderQuery in OrderDataMapper Bean


  }
}
