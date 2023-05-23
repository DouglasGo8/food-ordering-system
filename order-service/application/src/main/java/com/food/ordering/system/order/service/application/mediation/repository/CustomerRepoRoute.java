package com.food.ordering.system.order.service.application.mediation.repository;

import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
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
            .to("sql-stored:classpath:templates/findCustomerByIdFunction.sql?function=true") // done
            //.log(LoggingLevel.INFO, "${body['#result-set-1'].size}")
            // done CamelSqlRowCount
            .choice().when(simple("${body['#result-set-1'].size} == 0"))
              .log(LoggingLevel.INFO, "CustomerId not found in here")
              .throwException(new OrderDomainException("Couldn't find Customer id"))
            .otherwise()
              .log(LoggingLevel.INFO, "Found CustomerId")
            .end();
  }
}
