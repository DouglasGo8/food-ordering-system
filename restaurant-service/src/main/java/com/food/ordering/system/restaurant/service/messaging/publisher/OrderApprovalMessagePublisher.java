package com.food.ordering.system.restaurant.service.messaging.publisher;

import com.food.ordering.system.restaurant.service.domain.application.mapper.RestaurantMessagingResponseDataMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class OrderApprovalMessagePublisher extends RouteBuilder {

  @Override
  public void configure() {

    final String LOG_MESSAGE = """
             Receiving OrderApprovalEvent for order id: ${body.orderApproval.orderId.value}\s
             with status ${body.orderApproval.approvalStatus.name}
            \s""";

    // receives OrderApprovalEvent (Approved|Rejected)
    // from("seda:orderApprovalEventMessage").routeId("OrderApprovalMessagePublisher")
    from("seda:orderApprovalEventMessage"/*"direct:orderApprovalEventMessage"*/).routeId("OrderApprovalMessagePublisherRouteId")
            .log(LoggingLevel.INFO, LOG_MESSAGE)
            .bean(RestaurantMessagingResponseDataMapper::new)
            // will be consumed by RestaurantApprovalResponseKafkaListener in order service
            .to("kafka://{{restaurant.approval.topic.response}}")
            .end();
  }
}
