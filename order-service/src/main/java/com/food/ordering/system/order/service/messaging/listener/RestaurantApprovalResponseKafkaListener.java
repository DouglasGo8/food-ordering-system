package com.food.ordering.system.order.service.messaging.listener;

import com.food.ordering.system.order.service.dataaccess.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.domain.application.mapper.PaymentResponseDataMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
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




  }
}