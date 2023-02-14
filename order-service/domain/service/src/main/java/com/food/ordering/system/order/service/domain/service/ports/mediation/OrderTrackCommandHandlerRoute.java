package com.food.ordering.system.order.service.domain.service.ports.mediation;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class OrderTrackCommandHandlerRoute extends RouteBuilder {
  @Override
  public void configure() throws Exception {

    from("direct:orderTrackCommandHandler").routeId("orderTrackRoute")
            .log("Message from OrderTrackCommandHandlerRoute.class");
  }

  //@Handler
  //public TrackOrderResponse trackOrder(@Body @Valid TrackOrderQuery trackOrderQuery) {
  //  return null;
  //}
}
