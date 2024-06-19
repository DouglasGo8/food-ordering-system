package com.food.ordering.system.restaurant.service.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;


@ApplicationScoped
@NoArgsConstructor
public class RestaurantApprovalRequestMessageListener extends RouteBuilder {
  @Override
  public void configure() {



    // stops module 7:60Section implements and test RestaurantMessagingDataMapper

    // RestaurantApprovalRequestKafkaListener Implementation Sct::07::Vdo58
    // receives RestaurantApprovalRequest
    // possible kafka:Consumer @KafkaListener receiving a RestaurantApprovalRequestAvroModel
    from("direct:approveOrder").routeId("RestaurantApprovalRequestMessageListenerRouteId")
            .to("direct:persistOrderApproval") // returns OrderApprovalEvent
            .log("persistOrderApproval Route returns ${body}")
            // wireTap(seda:orderApprovalMessage)
            .end();
  }
}
