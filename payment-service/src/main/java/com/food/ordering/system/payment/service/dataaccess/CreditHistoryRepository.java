package com.food.ordering.system.payment.service.dataaccess;

import com.food.ordering.system.payment.service.domain.application.exception.PaymentApplicationServiceException;
import com.food.ordering.system.shared.domain.DomainConstants;

import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class CreditHistoryRepository extends RouteBuilder {

  // CreditHistory save(CreditHistory creditHistory)
  // Optional<List<CreditHistory>> findByCustomerId(CustomerId id);
  @Override
  public void configure() {

    from("direct:creditHistoryFindByCustomerId").routeId("CreditHistoryFindByRepositoryRouteId")
      .log(LoggingLevel.INFO,"Find Credit History to CustomerId: ${body.customerId}")
      .to("sql-stored:classpath:templates/findCustomerIdCreditHistory_fn.sql?function=true") // done
      .choice()
        .when(simple("${body['#result-set-1'].size} == 0"))
          .log(LoggingLevel.INFO, DomainConstants.CUSTOMER_NOT_FOUND_ID)
          .throwException(new PaymentApplicationServiceException("Couldn't find CustomerId"))
          //.otherwise()
          //.log(LoggingLevel.INFO, "Found CustomerId")
    .end();

    from("direct:saveCreditHistories").routeId("SaveCreditHistoriesRouteId")
            // must be the last item of the List
            .transform(simple("${exchangeProperty.creditHistories[last]}"))
            //.log("${body.transactionType}")
            .to("sql-stored:classpath:templates/insertCreditHistory.sql") // done
    .end();
  }
}
