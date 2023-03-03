package com.food.ordering.system.order.service.domain.service.ports.mediation;


import com.food.ordering.system.order.service.domain.service.mapper.OrderDataMapper;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@NoArgsConstructor
@ApplicationScoped
public class OrderTrackCommandHandlerRoute extends RouteBuilder {
  @Override
  public void configure() throws Exception {


    //from("direct:orderTrackCommandHandler").routeId("orderTrackRoute")
    //        .log("Message from OrderTrackCommandHandlerRoute.class");

    from("timer://myTm?period=10s&fixedRate=true")
            .bean(OrderDataMapper.class, "validateAndInitializeOrder"); // orderCreatedEvent Object
  }

  // will be a bean in the future
  //@Handler
  //public TrackOrderResponse trackOrder(@Body @Valid TrackOrderQuery trackOrderQuery) {
  //  return null;
  //}
}
