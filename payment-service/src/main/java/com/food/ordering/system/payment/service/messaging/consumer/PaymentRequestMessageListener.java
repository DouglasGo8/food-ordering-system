package com.food.ordering.system.payment.service.messaging.consumer;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;


@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestMessageListener extends RouteBuilder {

  // void completedPayment(PaymentRequest paymentRequest)
  // void cancelPayment(PaymentRequest paymentRequest)
  @Override
  public void configure() {

  }
}
