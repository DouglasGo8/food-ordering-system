package com.food.ordering.system.order.service.domain.service.ports.output.message.publisher.payment;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderCancelledPaymentPublisherRoute extends RouteBuilder {
  @Override
  public void configure() {
    // direct?seda?publish<OrderCancelledEvent> ?? send to OrderEvenMapper.publishEvent
  }
}
