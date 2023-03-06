package com.food.ordering.system.order.service.domain.service.ports.input.message.listener.payment;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class PaymentResponseMessageListenerRoute extends RouteBuilder {
  @Override
  public void configure() {

    // rest?direct?seda?kafka:paymentCompleted(PaymentResponse body)
    // rest?direct?seda?kafka:paymentCancelled(PaymentResponse body)

  }
}
