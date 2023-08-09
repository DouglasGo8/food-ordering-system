package com.food.ordering.system.payment.service.dataaccess;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class CreditHistoryRepository extends RouteBuilder {

  // CreditHistory save(CreditHistory creditHistory)
  // Optional<List<CreditHistory>> findByCustomerId(CustomerId id);
  @Override
  public void configure() {
    from("direct:creditHistoryRepository").routeId("CreditHistoryRepositoryRouteId")
            //.to("sql:findByCustomerId")
            //.choice().when("isEmpty")
            //  .log(LoggingLevel.ERROR, "Could not find credit history for CustomerId ${body.customerId}")
            //  .throwException(new PaymentApplicationServiceException("Could not find credit entry for CustomerId ${body.customerId}"))
            .transform(constant(" CreditHistory"))
            .end();
  }
}
