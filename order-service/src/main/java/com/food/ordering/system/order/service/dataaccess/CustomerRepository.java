package com.food.ordering.system.order.service.dataaccess;

import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.order.core.service.domain.exception.OrderDomainException;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class CustomerRepository extends RouteBuilder {
  @Override
  public void configure() {
    from("direct:checkCustomerCommandHandler").routeId("checkCustomerCMDH")
      .setProperty("customerId", simple("${body.customerId}"))
      .log(LoggingLevel.INFO,"findCustomerByIdFunction invoked")
      .to("sql-stored:classpath:templates/findCustomerByIdFunction.sql?function=true") // done
      // .log(LoggingLevel.INFO, "${body['#result-set-1'].size}")
      // done CamelSqlRowCount
      .choice()
        .when(simple("${body['#result-set-1'].size} == 0"))
          .log(LoggingLevel.INFO, DomainConstants.CUSTOMER_NOT_FOUND_ID)
          .throwException(new OrderDomainException("Couldn't find CustomerId"))
      //.otherwise()
      //  .log(LoggingLevel.INFO, "Found CustomerId")
      .end();
  }
}
