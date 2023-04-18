package com.food.ordering.system.order.service.application.mediation.service;


import com.food.ordering.system.order.service.application.mediation.dto.exception.ExceptionMapper;
import com.food.ordering.system.order.service.application.mediation.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainNotFound;
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

    // ExceptionHandler Advice Concept

    onException(OrderDomainException.class)
            .log("Exception occurs")
            .setHeader("codeAndReason", constant(500))
            .transform(exceptionMessage())
            .bean(ExceptionMapper.class); // ErrorDTO.class

    onException(OrderDomainNotFound.class)
            .bean(ExceptionMapper.class); // ErrorDTO.class

    //onException(InternalServerErrorException.class) ? validate usage

    // Apache Camel is a Hexagonal Architecture Killer ??
    // will compose the Apache Camel Pipeline
    // OrderDomainService
    // OrderDataMapper

    // OrderCreateHelper pipe
    from("direct:createOrderCommandHandler").routeId("CreateOrderCMDH")
            .setProperty("payload", body()) // CreateOrderCommandDTO
            .setProperty("restaurant", method(OrderDataMapper.class, "createOrderCommandToRestaurant"))
            //.multicast(new FlexibleAggregationStrategy<>().accumulateInCollection(ArrayList.class).pick(body()))
            //.parallelProcessing().parallelAggregate().stopOnException() // repository
            .multicast().stopOnException()
              .to("direct:checkCustomerCommandHandler", "direct:checkRestaurantCommandHandler")
            .end() // end multicast
            .bean(OrderDataMapper.class, "createOrderCommandToOrder") // returns Order Object
            .bean(OrderDataMapper.class, "validateAndInitializeOrder") // returns OrderCreatedEvent Object
            //.log("${body}")
            .setProperty("orderCreatedEvent", body())
            // ---------------------------------------------
            .transform(exchangeProperty("payload"))
            .setProperty("fail_msg", constant(""))
            .setProperty("address_id", constant("a8f8c751-e3b0-49bf-bbef-4588db030959")) // needs be fix
            // -------------------------------------------------
            .to("sql-stored:classpath:templates/insertOrders.sql")
            .setProperty("orderIdOut", simple("${body['result']}")) // result SpEL From PROCEDURE
            .log("Order created with id: ${exchangeProperty.orderIdOut}")
            // ---------------------------------------------
            .transform(exchangeProperty("payload"))
            // ------------------------------------------------
            // Removes code boilerplate from orderItemsToOrderItemEntities method
            .split(simple("${body.items}")).streaming(true).parallelProcessing()
            //.log("${exchangeProperty.orderIdOut}")
              .setProperty("orderItemId", simple("${uuid}"))
              .to("sql-stored:classpath:templates/insertOrderItems.sql")
            .end() // close Split
            // -----------------------------------------------------
            .transform(exchangeProperty("orderCreatedEvent"))
            // ---------------------------------------------------------
            //.wireTap("seda:publishOrderCreatedPayment?blockWhenFull=true&concurrentConsumers=5")
            .bean(OrderDataMapper.class, "orderToCreateOrderResponseDTO") // orderCreateResponseDTO
    .end();

  }

}
