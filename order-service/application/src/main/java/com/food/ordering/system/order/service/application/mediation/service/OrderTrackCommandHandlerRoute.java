package com.food.ordering.system.order.service.application.mediation.service;


import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@NoArgsConstructor
@ApplicationScoped
public class OrderTrackCommandHandlerRoute extends RouteBuilder {

  @Override
  public void configure() {

    from("direct:orderTrackCommandHandler").routeId("orderTrackRoute")
            .log("Message from ${body}")
            //.to("sql-stored:classpath:templates/getTrackingByIdFunction.sql") // returns Order
            // CamelSqlRow == 0 throws exception new OrderDomainNotFound("")
            //.bean(OrderDataMapper.class,"orderToTrackOrderResponse")
            .end();

  }

  // will be a bean in the future
  //@Handler
  //public TrackOrderResponse trackOrder(@Body @Valid TrackOrderQuery trackOrderQuery) {
  //  return null;
  //}
}
