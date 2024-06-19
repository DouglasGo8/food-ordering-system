package com.food.ordering.system.restaurant.service.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class OrderApprovalMessagePublisher extends RouteBuilder {

  @Override
  public void configure() {

    // receives RestaurantApprovalRequest
    from("seda:orderApprovalEventMessage").routeId("OrderApprovalMessagePublisher")
            .log(LoggingLevel.INFO, "Sending ${body} to Kafka")
            // to:kafka
            .end();
  }
}
