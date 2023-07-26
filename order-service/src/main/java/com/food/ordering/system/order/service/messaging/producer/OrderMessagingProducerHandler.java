package com.food.ordering.system.order.service.messaging.producer;

import com.food.ordering.system.order.service.application.service.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.application.service.mapper.OrderMessagingDataMapper;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderMessagingProducerHandler extends RouteBuilder {

  @Override
  public void configure() {


    //
    from("seda:publishOrderCreatedPayment").routeId("PublishOrderCreatedPayment")
            .setProperty("topic-key", simple("${body.order.id.value}"))
            .log("Received OrderCreatedEvent for order id: ${body.order.id.value}")
            // -----------------------------------------------------------------
            .bean(OrderMessagingDataMapper.class)
            .log(LoggingLevel.INFO,"${body}")
            // ----------------------------------------------------------------------------------------
            .choice().when(simple("${body.paymentOrderStatus} == 'PENDING'"))
              .log(LoggingLevel.INFO, "Received OrderCreatedEvent for order id ${body.id}")
            .otherwise()
              .log(LoggingLevel.INFO,"Received OrderCancelledEvent for order id ${body.id}")
            .end() // endChoice
            // ----------------------------------------------------------------------------------------------
            .setHeader(KafkaConstants.KEY, exchangeProperty("topic-key"))
            .to("kafka:{{payment-request-topic}}")
            .log(LoggingLevel.INFO, "PaymentRequestAvroModel sent to kafka for order id: ${body.id}")
            .end();



    // Only Test effect
    /*from("timer://myTimer?period=3s&fixedRate=true")
            .to("seda:publishOrderCreatedPayment")
            //.setHeader(KafkaConstants.KEY, simple("${body.id}"))

            .end();*/


    // Only Test effect
    /*from("kafka:payment-request")
            .log(LoggingLevel.INFO,"Message received from Kafka : ${body}-${threadName}")
            .log(LoggingLevel.INFO, "    on the topic ${headers[kafka.TOPIC]}")
            .log(LoggingLevel.INFO,"    on the partition ${headers[kafka.PARTITION]}")
            .log(LoggingLevel.INFO,"    with the offset ${headers[kafka.OFFSET]}")
            .log(LoggingLevel.INFO,"    with the key ${headers[kafka.KEY]}");*/



  }
}
