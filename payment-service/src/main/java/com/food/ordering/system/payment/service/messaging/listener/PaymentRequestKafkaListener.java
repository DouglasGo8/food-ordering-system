package com.food.ordering.system.payment.service.messaging.listener;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestKafkaListener extends RouteBuilder {

  @Override
  public void configure() throws Exception {


    from("kafka://{{payment.topic.response}}").routeId("PaymentRequestKafkaListenerRouteId")
            .log("Message received from Kafka : ${body}-${threadName}")
            .log("    on the topic ${headers[kafka.TOPIC]}")
            .log("    on the partition ${headers[kafka.PARTITION]}")
            .log("    with the offset ${headers[kafka.OFFSET]}")
            .log("    with the key ${headers[kafka.KEY]}")
            // choice to PaymentOrderStatus.PENDING call direct:completedPayment converting AvroRequestToPaymentRequest
            // otherwise call direct:cancelPayment converting AvroToPaymentRequest
            .end();

  }
}
