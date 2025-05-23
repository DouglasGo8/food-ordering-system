package com.food.ordering.system.order.service.domain.application;

import com.food.ordering.system.order.service.domain.application.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.application.mapper.RestaurantProductsMapper;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.core.exception.OrderNotFoundException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class OrderCreateCommandHandler extends RouteBuilder {


  @Override
  public void configure() {

    // ExceptionHandler Advice Concept

    onException(OrderDomainException.class)
            .log("Exception occurs")
            .setHeader("codeAndReason", constant(500))
            .transform(exceptionMessage());
    //.bean(ExceptionMapper.class); // ErrorDTO.class

    onException(OrderNotFoundException.class)
            .log("${body}");
    //.bean(ExceptionMapper.class); // ErrorDTO.class
    //
    // ==============================================================================
    // OrderDataAccessMapper.class
    // orderToOrderEntity method will be replaced by <-> Order To Procedure in DSL Camel
    // orderEntityToOrder?? new Order(); marshall??
    // CustomerDataAccessMapper.class
    // customerEntityToCustomer method will be replaced by <-> Customer To Procedure in DSL Camel
    // call function find_customer_byId
    //
    // ===================================================================================

    //onException(InternalServerErrorException.class) ? validate usage
    // OrderDomainService
    // OrderDataMapper

    // Same purpose OrderCreateCommandHandler/OrderCreateHelper class
    from("direct:createOrderCommandHandler").routeId("CreateOrderCommandHandlerRouteId")
            .setProperty("payload", body()) // CreateOrderCommandDTO
            .setProperty("restaurant", method(OrderDataMapper.class, "createOrderCommandToRestaurant"))
            //.multicast(new FlexibleAggregationStrategy<>().accumulateInCollection(ArrayList.class).pick(body()))
            //.parallelProcessing().parallelAggregate().stopOnException() // repository
            .multicast().stopOnException()
              .to("direct:checkCustomerCommandHandler", "direct:checkRestaurantCommandHandler")
            .end() // end multicast
            // --------------------------------------------------
            // Section 08 Video 65 at 2m49s
            // test all action bellows
            // -------------------------------------------------------
            .bean(RestaurantProductsMapper.class, "restaurantProductsIdsSQLClause")
            //.log("${body}")
            // ---------------------------------- -----------
            .to("direct:createRestaurantInformationMapper") //createRestaurantInformationMapper
            //.log("${body}")
            // ---------------------------------------------------
            .bean(RestaurantProductsMapper.class, "resultSetIteratorToRestaurant")
            .setProperty("restaurantInfo", body())
            .bean(OrderDataMapper.class, "createOrderCommandToOrder") // returns Order Object
            .bean(OrderDataMapper.class, "validateAndInitializeOrder") // returns OrderCreatedEvent Object
            .setProperty("orderCreatedEvent", body())
            .transform(exchangeProperty("payload")) // CreateOrderCommandDTO
            // ---------------------------------------------
            .to("direct:saveOrder")
            // -------------------------------------------------------
            .setProperty("orderIdOut", simple("${body['result']}")) // result SpEL From PROCEDURE
            .log("Order created with id: ${exchangeProperty.orderIdOut}")
            // ---------------------------------------------
            .transform(exchangeProperty("payload"))
            .to("direct:saveOrderItems")
            .transform(exchangeProperty("orderCreatedEvent"))
            // ------------------------------------------------------------------------
            .wireTap("seda:publishOrderCreatedPayment?blockWhenFull=true")
            // -------------------------------------------------------------------------------
            .bean(OrderDataMapper.class, "orderToCreateOrderResponseDTO") // orderCreateResponseDTO
            .log(LoggingLevel.INFO, "${body}")
            .end();

  }

}
