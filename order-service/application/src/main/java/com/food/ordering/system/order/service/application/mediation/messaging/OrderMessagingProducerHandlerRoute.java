package com.food.ordering.system.order.service.application.mediation.messaging;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderMessagingProducerHandlerRoute extends RouteBuilder {

  @Override
  public void configure() {

    from("direct:orderMessagingProducerHandler").routeId("OrderMessagingProducerHandler")
            .setProperty("topic-key", simple("${body.order.id.value}"))
            .bean(OrderMessagingDataMapper.class)
            .choice().when(simple("${body.paymentOrderStatus} == 'PENDING'"))
              .log("Received OrderCreatedEvent for order id ${body.id}")
            .otherwise()
              .log("Received OrderCancelledEvent for order id ${body.id}")
            //.setHeader(KafkaConstants.KEY, exchangeProperty("topic-key"))
            //.to("kafka")

            //.log( "PaymentRequestAvroModel sent to kafka for order id: ${body.id}");
            .end();


  }
}
