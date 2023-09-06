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

    from("direct:savePayment").routeId("SavePaymentRepositoryRouteId")
            //.to("sql:findByCustomerId")
            //.choice().when("isEmpty")
            //  .log(LoggingLevel.ERROR, "Could not find credit entry for CustomerId ${body.customerId}")
            //  .throwException(new PaymentApplicationServiceException("Could not find credit entry for CustomerId ${body.customerId}"))
            .transform(constant("Save the Payment"))
            .end();

    // refactory
    from("direct:paymentFindOrderId").routeId("FindOrderIdRouter")
            .transform(constant("Find Order Router"))
            //.choice().when("isEmpty")
            //.log(LoggingLevel.ERROR, "Payment with order id: ${body.??} could not be found!")
            //.throwException(new PaymentApplicationServiceException("Payment with order id body.orderId could not be found!"))
            .end();



  }
}
