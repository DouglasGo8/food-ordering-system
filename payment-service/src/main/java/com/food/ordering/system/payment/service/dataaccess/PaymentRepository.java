package com.food.ordering.system.payment.service.dataaccess;

import com.food.ordering.system.payment.service.domain.core.exception.PaymentApplicationServiceException;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRepository extends RouteBuilder {


  // Payment save(Payment payment)
  // Optional<Payment> findByOrderId(UUID orderId)
  @Override
  public void configure() {

    from("direct:paymentRepository").routeId("PaymentRepositoryRoute")
            //.to("sql:findByCustomerId")
            //.choice().when("isEmpty")
            //  .log(LoggingLevel.ERROR, "Could not find credit entry for CustomerId ${body.customerId}")
            //  .throwException(new PaymentApplicationServiceException("Could not find credit entry for CustomerId ${body.customerId}"))
            .transform(constant("Save the Payment"))
            .end();

  }
}
