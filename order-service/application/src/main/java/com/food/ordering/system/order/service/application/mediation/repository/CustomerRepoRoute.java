package com.food.ordering.system.order.service.application.mediation.repository;

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
            .to("sql-stored:classpath:templates/getCustomerByIdFunction.sql?function=true") // done
            //.log(LoggingLevel.INFO, "${body['#result-set-1'].size}")
            // done CamelSqlRowCount
            .choice().when(simple("${body['#result-set-1'].size} == 0"))
              .log(LoggingLevel.INFO, "CustomerId not found in here")
            //.throwException(new OrderDomainException(OrderDomainInfo.CUSTOMER_ID_NOT_FOUND))
            .otherwise()
              .log(LoggingLevel.INFO, "Found CustomerId")
            .end();
  }
}
