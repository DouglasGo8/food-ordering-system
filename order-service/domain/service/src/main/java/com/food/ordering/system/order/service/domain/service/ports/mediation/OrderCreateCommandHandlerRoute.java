package com.food.ordering.system.order.service.domain.service.ports.mediation;


import com.food.ordering.system.order.service.domain.core.common.OrderDomainInfo;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.service.mapper.OrderDataMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.FlexibleAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class OrderCreateCommandHandlerRoute extends RouteBuilder {


  @Override
  public void configure() {

    // will compose the Apache Camel Pipeline
    // OrderDomainService
    // OrderRepository replaced by DSL Camel
    // CustomerRepository replaced by DSL Camel
    // RestaurantRepository replaced by DSL Camel
    // OrderDataMapper

    from("direct:createOrderCommandHandler").routeId("createOrderCMDH")
            .setHeader("payload", body())
            .setHeader("restaurant",  method(OrderDataMapper.class, "createOrderCommandToRestaurant"))
            .multicast().stopOnException()
              .to( "direct:checkCustomerCommandHandler","direct:checkRestaurantCommandHandler")
            .end() // end multicast
            .transform(header("payload"))
            .bean(OrderDataMapper::new, "createOrderCommandToOrder") // order Object
            .log(LoggingLevel.INFO, "${body}-${header.restaurant}")

            .end();


    from("direct:checkCustomerCommandHandler").routeId("checkCustomerCMDH")
            //.transacted() // no makes sense for read operations
            // checkCustomer.jdbc.getCustomerById
            //.setHeader("payload", body())
            //.to("sql-stored:get_customer_byId(java.sql.Types.INTEGER ${header.srcValue})")
            .to("sql-stored:classpath:templates/getCustomerByIdFunction.sql") // done
            //.log("${body.size}");
            // done CamelSqlRowCount
            .choice().when(simple("${body.size} == 0"))
              .log(LoggingLevel.INFO, "CustomerId not found in here")
              //.throwException(new OrderDomainException(OrderDomainInfo.CUSTOMER_ID_NOT_FOUND))
            .otherwise()
              .log(LoggingLevel.INFO, "${body}")

            // checkRestaurant with Body Restaurant
            //.bean(OrderDomainService.class, "validateAndInitiateOrder") // OrderCreatedEvent
            //.to("sql-stored:classpath:templates/saveOrderProcedure.sql")
            //.choice().when(simple("body proc returned is not null"))
            //.otherwise().log("new Order Id saved in DB")
            //.bean(OrderDataMapper::new, "orderToCreateOrderResponseDTO")
            .end();

    from("direct:checkRestaurantCommandHandler").routeId("checkRestaurantCMDH")
            // com.food.**.Restaurant@11053528
            .to("sql-stored:classpath:templates/getRestaurantByIdFunction.sql")
            .choice().when(simple("${body.size} == 0"))
              .log(LoggingLevel.INFO, "RestaurantId not found in here")
              //.throwException(new OrderDomainException(OrderDomainInfo.RESTAURANT_ID_NOT_FOUND))
            .otherwise()
              .log(LoggingLevel.INFO, "${body}") // id is BaseId
            .end();
    //.log(LoggingLevel.INFO, "message from OrderCreateCommandHandler.class");

  }


  //@Handler
  //public CreateOrderResponse createOrder(@Body @Valid CreateOrderCommand createOrderCommand) {
  //  return null;
  //}
}
