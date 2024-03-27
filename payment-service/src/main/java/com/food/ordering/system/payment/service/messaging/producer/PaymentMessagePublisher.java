package com.food.ordering.system.payment.service.messaging.producer;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class PaymentMessagePublisher extends RouteBuilder {

  // supports both Payment*CompletedMessageListener, *CanceledMessagePublisher and *FailedMessagePublisher
  @Override
  public void configure() {

    from("seda:publishPaymentEvent").routeId("PublishPaymentEventRouter")
            .log("Publishing payment Event with payment id: and order id")
            .end();
  }
}
