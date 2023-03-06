package com.food.ordering.system.order.service.domain.service.ports.output.repository;

import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@NoArgsConstructor
@ApplicationScoped
public class CustomerRepoRoute extends RouteBuilder {
  @Override
  public void configure() {
    from("direct:checkCustomerCommandHandler").routeId("checkCustomerCMDH")
            //.transacted() // no makes sense for read operations
            // checkCustomer.jdbc.getCustomerById
            //.setHeader("payload", body())
            //.to("sql-stored:get_customer_byId(java.sql.Types.INTEGER ${header.srcValue})")
            .to("sql-stored:classpath:templates/getCustomerByIdFunction.sql") // done
            //.log("${body.size}");
            // done CamelSqlRowCount
            .choice().when(simple("${header.CamelSqlRowCount} == 0"))
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
  }
}
