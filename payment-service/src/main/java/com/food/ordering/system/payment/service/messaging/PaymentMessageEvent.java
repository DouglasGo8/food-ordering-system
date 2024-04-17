package com.food.ordering.system.payment.service.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
@NoArgsConstructor
public class PaymentMessageEvent extends RouteBuilder {
  @Override
  public void configure() {

    // avoid if (instanceof PaymentCompletedOrCancelEvent
    from("direct:paymentMessagePublisher" /*"seda:paymentMessagePublisher"*/).routeId("PaymentMessageFireEvent")
            .log("Publishing payment Event with payment id: ${body.payment.paymentId.value} and order id ${body.orderId.value}")
            // sending one of PaymentCompletedEvent/PaymentCancelledEvent/PaymentFailedEvent
            .end();
  }
}
