package com.food.ordering.system.order.service.application.mediation.service;


import com.food.ordering.system.order.service.application.mediation.dto.exception.ErrorDTO;
import com.food.ordering.system.order.service.application.mediation.dto.exception.ExceptionMapper;
import com.food.ordering.system.order.service.application.mediation.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.core.OrderDomainService;
import com.food.ordering.system.order.service.domain.core.common.OrderDomainInfo;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainNotFound;
import io.netty.handler.codec.http.HttpStatusClass;
import io.netty.handler.codec.http2.Http2Error;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.FlexibleAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import java.util.List;

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

    // will compose the Apache Camel Pipeline
    // OrderDomainService
    // OrderDataMapper

    // OrderCreateHelper pipe
    from("direct:createOrderCommandHandler").routeId("createOrderCMDH")
            .setProperty("payload", body()) // CreateOrderCommandDTO
            .setProperty("restaurant", method(OrderDataMapper.class, "createOrderCommandToRestaurant"))
            //.multicast(new FlexibleAggregationStrategy<>().accumulateInCollection(ArrayList.class).pick(body()))
            //.parallelProcessing().parallelAggregate().stopOnException() // repository
            .multicast().stopOnException()
              .to("direct:checkCustomerCommandHandler", "direct:checkRestaurantCommandHandler")
            .end() // end multicast
            .bean(OrderDataMapper.class, "createOrderCommandToOrder") // returns Order Object
            .bean(OrderDataMapper.class, "validateAndInitializeOrder") // returns OrderCreatedEvent Object
            /*.setProperty("orderCreatedEvent", body())
            .transform(exchangeProperty("payload"))
            .to("sql-stored:classpath:templates/insertOrders.sql")
            .setProperty("orderIdOut", simple("${body['result']}")) // result SpEL From PROCEDURE
            .log("Order created with id: ${exchangeProperty.orderIdOut}")
            .transform(exchangeProperty("payload"))
            // Removes code boilerplate from orderItemsToOrderItemEntities method
            .split(simple("${body.items}")).streaming(true).parallelProcessing()
            //  .log("${exchangeProperty.orderIdOut}")
              .to("sql-stored:classpath:templates/insertOrderItems.sql")
            .end() // close Split
            .transform(exchangeProperty("orderCreatedEvent"))
            //.wireTap("seda:publishOrderCreatedPayment?blockWhenFull=true&concurrentConsumers=5")
            .bean(OrderDataMapper.class, "orderToCreateOrderResponseDTO") // orderCreateResponseDTO*/
            .log("${body}")
    .end();



  }

}
