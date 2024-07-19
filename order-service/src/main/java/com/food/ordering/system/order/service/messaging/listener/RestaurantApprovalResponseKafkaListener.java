package com.food.ordering.system.order.service.messaging.listener;

import com.food.ordering.system.order.service.dataaccess.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.domain.application.OrderDomainServiceImpl;
import com.food.ordering.system.order.service.domain.application.mapper.RestaurantMessagingDataMapper;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.InMemorySagaService;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantApprovalResponseKafkaListener extends RouteBuilder {
  @Override
  public void configure() throws Exception {

    // Step 1
    super.getContext().addService(new InMemorySagaService());

    // Kafka Consumer
    // calls RestaurantApprovalResponseMessageListenerImpl
    // from("kafka:topic?") RestaurantApprovalResponseAvroModel

    // .choice .when(OrderApprovalStatus == APPROVED)
    // log("Processing approved order for order id: {}")
    // RestaurantApprovalResponseMessageListener.class approvalResponseAvroModelToApprovalResponse
    // when (OrderApprovalStatus == REJECTED)
    // log("Processing rejected for order id: {}, with fail messages

    // Receives RestaurantApprovalResponseAvroModel
    from("direct:mockRestaurantResponseKafkaListener")
            // from("kafka:")
            //.log("Message received from Kafka : ${body}-${threadName}")
            //.log("    on the topic ${headers[kafka.TOPIC]}")
            //.log("    on the partition ${headers[kafka.PARTITION]}")
            // .log("    with the offset ${headers[kafka.OFFSET]}")
            //.log("    with the key ${headers[kafka.KEY]}");
            // ------------------------------------------------------------------------
            .bean(RestaurantMessagingDataMapper.class, "approvalResponseAvroModelToApprovalResponse") // from Avro Status
            .setVariable("sagaId", simple("${body.sagaId}"))
            .setVariable("orderApprovalStatus", simple("${body.orderApprovalStatus}"))
            // -----------------------------------------------------------------------------
            .saga() // Apache Camel Abstract All the *Saga* classes implementation
              .to("direct:findOrderAddressAndItemsById") // avoid call same method twice
              .bean(OrderDataAccessMapper::new)
              //.log("${body}")
              .to("direct:processOrderApproval")
            .end();


    // Represents RestaurantApprovalResponseMessageListenerImpl.class
    from("direct:processOrderApproval").routeId("ProcessOrderApprovalRouteId")
            .saga()
            // -------------------SAGA CREATION START ---------------------------------------
              .propagation(SagaPropagation.MANDATORY)
              .option("order",  body())
              .option("sagaId", variable("sagaId"))
              .completion("direct:orderApproved") // represents process method from OrderApprovalSaga.class
              .compensation("direct:orderRejected") // represents rollback method from OrderApprovalSaga.class
            // -------------------SAGA CREATION END ------------------------------------------------------------
            // Represents RestaurantApprovalResponseMessageListenerImpl.orderRejected method
            .choice().when(simple("${variable.orderApprovalStatus} == 'REJECTED'"))
              .setVariable("failure_msg", simple("${body.failureMessages}"))
              .bean(OrderDomainServiceImpl.class, "cancelOrderPayment") // changes OrderStatus to CANCELLING and returns OrderCancelledEvent
              .log(LoggingLevel.ERROR, "!!!BOOM!!! Publishing order cancelled for order id: ${body.order.id.value} with failure messages {under_construction}")
              //.wireTap("kafka") //
              .throwException(new OrderDomainException("Order is roll backed due to CANCELLING Status"))
            // Represents RestaurantApprovalResponseMessageListenerImpl.orderApproved method
            .otherwise()
              .log(LoggingLevel.INFO, "Order is approved for order id: ${body.id.value}")
              .bean(OrderDomainServiceImpl.class, "approveOrder") // changes OrderStatus to APPROVED and returns Void
            .end();


    from("direct:orderApproved").routeId("OrderApprovedRouteId")
            .log(LoggingLevel.INFO, "Approving order with id: ${header.order.id.value}")
            .transform(header("order"))
            .to("direct:saveOrderSaga") // saves WiTH PAID/SUCCESS Status
            .log("Order with id: ${header.order.id.value} is approved")
            .end();

    from("direct:orderRejected").routeId("OrderRejectedRouteId")
            .transform(header("order"))
            .log(LoggingLevel.ERROR, "Cancelling order with id: ${header.order.id.value}")
            .to("direct:saveOrderSaga") // saves WiTH CANCELLED/FAILED Status
            .log(LoggingLevel.ERROR, "Order with id: ${header.order.id.value} is cancelling")
            .end();

  }
}