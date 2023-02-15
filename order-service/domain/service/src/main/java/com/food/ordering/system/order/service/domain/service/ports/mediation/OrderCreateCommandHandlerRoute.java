package com.food.ordering.system.order.service.domain.service.ports.mediation;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
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
    // OrderRepository
    // CustomerRepository
    // RestaurantRepository
    // OrderDataMapper

    from("direct:orderCreateCommandHandler").routeId("orderCreateRoute")
            //.transacted() // no makes sense for read operations
            // checkCustomer.jdbc.getCustomerById
            //.to("sql-stored:get_customer_byId(java.sql.Types.INTEGER ${header.srcValue})")
            .to("sql-stored:classpath:templates/getCustomerByIdFunction.sql")
            .log(LoggingLevel.INFO, "${body}");
            //.log(LoggingLevel.INFO, "message from OrderCreateCommandHandler.class");
  }



  //@Handler
  //public CreateOrderResponse createOrder(@Body @Valid CreateOrderCommand createOrderCommand) {
  //  return null;
  //}
}
