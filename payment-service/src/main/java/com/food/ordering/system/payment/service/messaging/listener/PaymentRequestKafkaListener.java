package com.food.ordering.system.payment.service.messaging.listener;


import com.food.ordering.system.payment.service.domain.application.mapper.PaymentMessagingAvroRequestDataMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestKafkaListener extends RouteBuilder {

  @Override
  public void configure() {


    from("kafka://{{payment.request.topic}}").routeId("PaymentRequestKafkaListenerRouteId")
            /*.log("Message received from Kafka : ${body}")
            .log("    on the topic ${headers[kafka.TOPIC]}")
            .log("    on the partition ${headers[kafka.PARTITION]}")
            .log("    with the offset ${headers[kafka.OFFSET]}")
            .log("    with the key ${headers[kafka.KEY]}")
             */
            .choice()
              .when(simple("${body.paymentOrderStatus} == 'PENDING'"))
                .transform(method(PaymentMessagingAvroRequestDataMapper.class))
                .to("direct:completedPayment")
              .otherwise()
                .log("CANCELLED PAYMENT")
            // choice to PaymentOrderStatus.PENDING call direct:completedPayment converting AvroRequestToPaymentRequest
            // otherwise call direct:cancelPayment converting AvroToPaymentRequest
            .end();

  }
}
