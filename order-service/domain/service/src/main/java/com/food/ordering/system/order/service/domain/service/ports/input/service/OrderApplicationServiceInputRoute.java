package com.food.ordering.system.order.service.domain.service.ports.input.service;

import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.service.dto.track.TrackOrderResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;

/**
 * OrderApplicationServiceImpl/OrderApplicationService was abstracted by Camel Routes
 * in REST DSL Composition
 */
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class OrderApplicationServiceInputRoute extends RouteBuilder {

  @Override
  public void configure() {
    // rest/post/createOrder(CreateOrderCommand) implicit Input Camel Binding with CreateOrderResponse returns
    // @Valid CreateOrderCommand in OrderDataMapper Bean
    // trackOrder(TrackOrderQuery trackOrderQuery) implicit Input Camel Binding with TrackOrderResponse return
    // @Valid TrackOrderQuery in OrderDataMapper Bean


    restConfiguration().component("netty-http").host("localhost")
            .port(12080).bindingMode(RestBindingMode.auto);


    //rest("/say").get("/hello").to("direct:hello");

    rest("/order/api/v1")
            .post("/createOrder").consumes("application/json").type(CreateOrderCommand.class)
            //.outType(CreateOrderResponse.class)
            .to("direct:orderCreateCommandHandler");
    //.get("/trackOrder").outType(TrackOrderResponse.class)
    //.to("direct:orderTrackCommandHandler");

    // composition


    //from("direct:hello").transform(constant("hello Mrs"));

  }
}
