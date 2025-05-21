package com.food.ordering.system.restaurant.service.messaging;

import com.food.ordering.system.restaurant.service.domain.application.mapper.RestaurantMessagingRequestDataMapper;
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
            Receiving OrderApprovalEvent for order id: ${body.orderApproval.orderId.value} 
            with status ${body.orderApproval.approvalStatus.name}
            """;

    // receives OrderApprovalEvent (Approved|Rejected)
    // from("seda:orderApprovalEventMessage").routeId("OrderApprovalMessagePublisher")
    from("direct:orderApprovalEventMessage").routeId("OrderApprovalMessagePublisherRouteId")
            .log(LoggingLevel.INFO, LOG_MESSAGE)
            .bean(RestaurantMessagingResponseDataMapper::new)
            // to:kafka // topic restaurant-approval-request
            .end();
  }
}
