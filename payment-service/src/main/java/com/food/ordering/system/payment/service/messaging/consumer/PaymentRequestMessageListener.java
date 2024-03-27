package com.food.ordering.system.payment.service.messaging.consumer;

import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestMessageListener extends RouteBuilder {

  // void completedPayment(PaymentRequest paymentRequest)
  // void cancelPayment(PaymentRequest paymentRequest)
  @Override
  public void configure() {


    //kafka:topic?
    from("direct:completedPayment").routeId("CompletedPaymentRouter")
            .log(LoggingLevel.INFO, "Received..")
            .to("direct:persistPayment") // paymentEvent

            //.to("seda:publishPaymentEvent") // PaymentEvent*Cancel*Completed*Failed

            .end();



    //kafka:topic?
    from("direct:cancelPayment").routeId("CancelPaymentRouter")
            .log(LoggingLevel.INFO, "Received...")
            .to("direct:persistCancelPayment") // paymentEvent
            //.to("seda:publishPaymentEvent") // PaymentEvent*Cancel*Completed*Failed
            .end();

  }
}
