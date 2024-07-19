package com.food.ordering.system.order.service.messaging.listener;

import com.food.ordering.system.order.service.dataaccess.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.domain.application.OrderDomainServiceImpl;
import com.food.ordering.system.order.service.domain.application.mapper.PaymentResponseDataMapper;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.InMemorySagaService;

@NoArgsConstructor
@ApplicationScoped
public class PaymentResponseKafkaListener extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    //

    // Step 1
    super.getContext().addService(new InMemorySagaService());

    // Kafka Consumer
    // SAGA structure possibly will be here
    //from("kafka:topic?")// PaymentResponseAvroModel
    //.log("Message received from Kafka : ${body}-${threadName}")
    //.log("    on the topic ${headers[kafka.TOPIC]}")
    //.log("    on the partition ${headers[kafka.PARTITION]}")
    // .log("    with the offset ${headers[kafka.OFFSET]}")
    //.log("    with the key ${headers[kafka.KEY]}");
    // .to("direct:paymentResponse")

    //
    //.choice when(PaymentStatus == COMPLETED)
    // log("Processing successful payment for order id: ${body.id})
    // paymentResponseMessageListener.paymentCompleted(PaymentResponseDataMapper.class)
    // when(PaymentStatus == CANCELED Or PaymentStatus CANCELED)
    // log("Processing unsuccessful payment for order id{}, ${body.id}
    // paymentResponseMessageListener.paymentCancelled(PaymentResponseDataMapper.class)

    from("direct:mockPaymentResponseKafkaListener")
            // from("kafka:")
            //.log("Message received from Kafka : ${body}-${threadName}")
            //.log("    on the topic ${headers[kafka.TOPIC]}")
            //.log("    on the partition ${headers[kafka.PARTITION]}")
            // .log("    with the offset ${headers[kafka.OFFSET]}")
            //.log("    with the key ${headers[kafka.KEY]}");
            // ------------------------------------------------------------------------
            .bean(PaymentResponseDataMapper::new) // from Avro Status
            .setVariable("sagaId", simple("${body.sagaId}"))
            .setVariable("paymentStatus", simple("${body.paymentStatus}"))
            // -----------------------------------------------------------------------------
            .saga() // Apache Camel Abstract All the *Saga* classes implementation
              .to("direct:findOrderAddressAndItemsById")
              .bean(OrderDataAccessMapper::new)
              .to("direct:processPayment")
            .end();

    // Represents PaymentResponseMessageListenerImpl.class implementation
    from("direct:processPayment").routeId("ProcessPaymentRouteId")
            .saga()
              // -------------------SAGA CREATION START ---------------------------------------
              .propagation(SagaPropagation.MANDATORY)
              .option("order",  body())
              .option("sagaId", variable("sagaId"))
              .completion("direct:paymentCompleted") // represents process method from OrderPaymentSaga.class
              .compensation("direct:paymentCancelled") // represents rollback method from OrderPaymentSaga.class
              // -------------------SAGA CREATION END ------------------------------------------------------------
              .choice().when(simple("${variable.paymentStatus} == 'CANCELLED'"))
                .log(LoggingLevel.ERROR, "Order is roll backed for order id: ${body.id.value} with failure messages: {under_construction}")
                .bean(OrderDomainServiceImpl.class, "cancelOrder") // changes to CANCELLED and returns void
                .throwException(new OrderDomainException("Order is roll backed due to CANCELLED Status"))
              .otherwise()
                .log(LoggingLevel.INFO, "Completing payment for order with id: ${body.id.value}")
                .bean(OrderDomainServiceImpl.class, "payOrder") // changes to PAID and returns OrderPaidEvent
                //.wireTap("kafka")
            .end();

    from("direct:paymentCompleted").routeId("PaymentCompletedRouteId") // Represents PaymentResponseMessageListenerImpl.paymentCompleted method
            .transform(header("order"))
            .to("direct:saveOrderSaga") // saves WiTH PAID/SUCCESS Status
            .log(LoggingLevel.INFO, "Order with id: ${header.order.id.value} is paid")
            .log("Publishing OrderPaidEvent for order id. ${header.order.id.value}")

            .end();

    from("direct:paymentCancelled").routeId("PaymentCancelledRouteId") // Represents PaymentResponseMessageListenerImpl.paymentCancelled method
            .transform(header("order"))
            .log(LoggingLevel.ERROR, "!!!BOOM!!! Cancelling Order Id: ${header.order.id.value}")
            .to("direct:saveOrderSaga") // saves WiTH CANCELLED/FAILED Status
            .log(LoggingLevel.ERROR, "Order with id: ${header.order.id.value} is cancelled")
            .end();
  }
}
