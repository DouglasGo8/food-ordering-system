package com.food.ordering.system.payment.service.messaging.publisher;

import com.food.ordering.system.payment.service.domain.application.mapper.PaymentMessagingAvroDataMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
@NoArgsConstructor
public class PaymentKafkaMessagePublisher extends RouteBuilder {
  @Override
  public void configure() {

    // avoid if (instanceof PaymentCompletedOrCancelEvent
    // Represents Payment*KafkaMessagePublisher
    from("direct:paymentMessagePublisher" /*"seda:paymentMessagePublisher"*/).routeId("PaymentMessagePublisherRouteId")
            .setVariable("topic-key", simple("${body.payment.orderId.value}"))
            .log("Publishing a Payment${body.payment.paymentStatus}Event with payment id: ${body.payment.id.value} and order id ${body.payment.orderId.value}")
            // sending one of PaymentCompletedEvent/PaymentCancelledEvent/PaymentFailedEvent
            .bean(PaymentMessagingAvroDataMapper::new)
            .log("${body}")
            //.setHeader(KafkaConstants.KEY, variable("topic-key"))
            /* Camel can abstract the below declarations

            <T> ListenableFutureCallback<SendResult<String, T>>getKafkaCallback() {
              return new ListenableFutureCallback() {
              }
            };

            kafkaProducer.send(topic-name, key, eventData,
              class.getKafkaCallback(topic, eventData, key, "alias"))

            with the process method


             .process(e -> {
              var recordMetadataList = (List<RecordMetadata>) e.getIn().getHeader(recordMedataHeader);
              if (null != recordMetadataList)
                recordMetadataList.forEach(c -> log.info("Receiving success from Kafka from orderId {} \n" +
                        "Topic - {}\n " +
                        "Offset: {}\n " +
                        "Timestamp: {}\n" +
                        "Partition: {}\n", c.topic(), c.offset(), c.timestamp(), c.partition()));
            });


             */


            //.to("kafka://topic")
            .end();
  }
}
