package com.food.ordering.system.order.service.domain.service.ports.output.respository;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderRepositoryRoute extends RouteBuilder {
  @Override
  public void configure() {
    // direct:saveOrder/Jdbc Component in Postgres Procedure returning Order body
    // direct:findByTrackingId(TrackingId param) Jdbc Component in Postgres Function refcursorfunc() returning Optional<Order>
  }
}
