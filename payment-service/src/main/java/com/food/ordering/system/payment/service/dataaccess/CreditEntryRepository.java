package com.food.ordering.system.payment.service.dataaccess;

import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.order.core.service.domain.exception.OrderDomainException;
import lombok.AllArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
@AllArgsConstructor
public class CreditEntryRepository extends RouteBuilder {

  // CreditEntry save(CreditEntry creditEntry)
  // Optional<CreditEntry> findByCustomerId(CustomerId customerId)
  @Override
  public void configure() {
    from("direct:creditEntryFindByCustomerId").routeId("CreditEntryFindByCustomerIdRouter")
      .log(LoggingLevel.INFO,"Find Credit Entry to CustomerId: ${body.customerId}")
      .to("sql-stored:classpath:templates/findCustomerIdCreditEntry_fn.sql?function=true") // done
      //.transform(constant("CreditEntry"))
      .choice()
        .when(simple("${body['#result-set-1'].size} == 0"))
          .log(LoggingLevel.INFO, DomainConstants.CUSTOMER_NOT_FOUND_ID)
          .throwException(new OrderDomainException("Couldn't find CustomerId"))
            //.otherwise()
            //.log(LoggingLevel.INFO, "Found CustomerId")
      .end();

    from("direct:saveCreditEntry").routeId("SaveCreditEntryRepositoryRouter")
            .transform(constant("SaveCredit"))
            .end();
  }
}
