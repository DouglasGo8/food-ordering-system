package com.food.ordering.system.order.service.domain.service.ports.input.mediation;


import com.food.ordering.system.order.service.domain.service.mapper.OrderDataMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class OrderCreateCommandHandlerRoute extends RouteBuilder {


  @Override
  public void configure() {

    // will compose the Apache Camel Pipeline
    // OrderDomainService
    // OrderDataMapper

    from("direct:createOrderCommandHandler").routeId("createOrderCMDH")
            .setProperty("payload", body())
            //.setProperty("restaurant", method(OrderDataMapper.class, "createOrderCommandToRestaurant"))
            .multicast().stopOnException()
              .to("direct:checkCustomerCommandHandler", "direct:checkRestaurantCommandHandler")
            .end() // end multicast
            .transform(simple("${exchangeProperty.payload}"))
            //.log("${body}")
            .bean(OrderDataMapper.class, "createOrderCommandToOrder") // order Object
            .setProperty("order", body())
            //.log(LoggingLevel.INFO, "${body}-${exchangeProperty.restaurant}")
            .bean(OrderDataMapper.class, "validateAndInitializeOrder") // orderCreatedEvent Object
            //sql-stored:saveOrder // must return new Order from DB
            // log("Order is created with id: ${body.customerId}")
            // exception from DB could not save order
            //.wireTap("seda:publishOrderCreatedPayment?blockWhenFull=true&concurrentConsumers=5")
            //.setProperty("message", constant("Order Successfully created"))
            //.bean(OrderDataMapper.class, "orderToCreateOrderResponseDTO") // orderCreateResponseDTO
            .log("${body}")
            .end();

  }

}
