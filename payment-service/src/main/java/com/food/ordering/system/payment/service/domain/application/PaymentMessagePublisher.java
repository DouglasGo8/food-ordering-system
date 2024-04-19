package com.food.ordering.system.payment.service.domain.application;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;


@NoArgsConstructor
@ApplicationScoped
public class PaymentMessagePublisher extends RouteBuilder {

  // void completedPayment(PaymentRequest paymentRequest)
  // void cancelPayment(PaymentRequest paymentRequest)
  @Override
  public void configure() {

    // Receives PaymentEvent
    // represents completePayment method
    from("direct:completedPayment").routeId("PublishPaymentEvent")
            .to("direct:persistPayment") // paymentEvent
            //.wireTap("seda:paymentMessagePublisher")
            .end();

    from("direct:cancelPayment").routeId("CancelPaymentEvent")
            .to("direct:persistCancelPayment") // paymentEvent
            //.wireTap("seda:paymentMessagePublisher")
            .end();



  }
}
