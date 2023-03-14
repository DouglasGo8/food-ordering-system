package com.food.ordering.system.order.service.application.rest;

import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderResponseDTO;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderController extends RouteBuilder {


  @Override
  public void configure() {
    // rest/post/createOrder(CreateOrderCommand) implicit Input Camel Binding with CreateOrderResponse returns
    // @Valid CreateOrderCommand in OrderDataMapper Bean
    // trackOrder(TrackOrderQuery trackOrderQuery) implicit Input Camel Binding with TrackOrderResponse return
    // @Valid TrackOrderQuery in OrderDataMapper Bean
    restConfiguration().component("netty-http").contextPath("/orders")
            .dataFormatProperty("prettyPrint", "true")
            .host("localhost").port(12080).bindingMode(RestBindingMode.auto);

    //rest("/say").get("/hello").to("direct:hello");

    //from("direct:hello").transform(constant("hi"));

    rest("/api/v1/")
            .post("/createOrder")
            .consumes("application/json")
            .type(CreateOrderCommandDTO.class)
            .outType(CreateOrderResponseDTO.class)
            .to("direct:createOrderCommandHandler");
    //.get("/trackOrder").outType(TrackOrderResponse.class)
    //.to("direct:orderTrackCommandHandler");
    // composition


  }
}
