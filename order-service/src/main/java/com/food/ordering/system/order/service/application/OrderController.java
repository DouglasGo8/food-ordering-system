package com.food.ordering.system.order.service.application;

import com.food.ordering.system.order.service.application.service.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.application.service.dto.create.CreateOrderResponseDTO;
import com.food.ordering.system.order.service.application.service.dto.track.TrackOrderResponseDTO;
import lombok.NoArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderController extends RouteBuilder {


  @Override
  public void configure() {
    // rest/post/createOrder(CreateOrderCommand) implicit Input Camel Binding with CreateOrderResponse returns
    // @Valid CreateOrderCommandDTO in OrderDataMapper Bean
    // trackOrder(TrackOrderQuery trackOrderQuery) implicit Input Camel Binding with TrackOrderResponse return
    // @Valid TrackOrderQuery in OrderDataMapper Bean
    restConfiguration().component("netty-http")
            // ---------- CONTEXT PATH BEGIN ---------------------
            .contextPath("/orders")
            // ---------- CONTEXT PATH END -----------------------
            .dataFormatProperty("prettyPrint", "true")
            .host("0.0.0.0").port(12080).bindingMode(RestBindingMode.auto);

    //rest("/say").get("/hello").to("direct:hello");

    //from("direct:hello").transform(constant("hi"));

    rest().path("/api/v1/")
          .post("/createOrder")
            .consumes("application/json")
            .type(CreateOrderCommandDTO.class)
            .outType(CreateOrderResponseDTO.class)
            .to("direct:createOrderRouteInline")
          .get("/trackingId/{uuid}")
            .outType(TrackOrderResponseDTO.class)
            .to("direct:trackingIdRouteInline");


    from("direct:createOrderRouteInline").routeId("createOrderRouteInline")
            .log(LoggingLevel.INFO, "Creating order for customer: ${body.customerId} at restaurant: ${body.restaurantId}")
            //.to("direct:createOrderCommandHandler")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant("201"))
            //.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/vnd.api.v1+json"))
            .end();

    from("direct:trackingIdRouteInline").routeId("trackingIdRouteInline")
            .to("direct:orderTrackCommandHandler")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant("200"))
            //.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            //.setHeader(Exchange.CONTENT_TYPE, constant("application/vnd.api.v1+json"))
            .end();

    //.get("/trackOrder").outType(TrackOrderResponse.class)
    //.to("direct:orderTrackCommandHandler");
    // composition


  }
}