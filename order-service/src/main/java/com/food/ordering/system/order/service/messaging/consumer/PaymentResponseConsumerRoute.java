package com.food.ordering.system.order.service.messaging.consumer;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class PaymentResponseConsumerRoute extends RouteBuilder {

  @Override
  public void configure() {
    //

    //from("kafka:topic?")// PaymentResponseAvroModel
    //.log("Message received from Kafka : ${body}-${threadName}")
    //.log("    on the topic ${headers[kafka.TOPIC]}")
    //.log("    on the partition ${headers[kafka.PARTITION]}")
    // .log("    with the offset ${headers[kafka.OFFSET]}")
    //.log("    with the key ${headers[kafka.KEY]}");
    //.choice when(PaymentStatus == COMPLETED)
    // log("Processing successful payment for order id: ${body.id})
    // paymentResponseMessageListener.paymentCompleted(PaymentResponseDataMapper.class)
    // when(PaymentStatus == CANCELED Or PaymentStatus CANCELED)
    // log("Processing unsuccessful payment for order id{}, ${body.id}
    // paymentResponseMessageListener.paymentCancelled(PaymentResponseDataMapper.class)
  }
}
