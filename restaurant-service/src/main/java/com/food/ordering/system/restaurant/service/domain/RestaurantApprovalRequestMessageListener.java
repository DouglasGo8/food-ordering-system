package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.restaurant.service.domain.application.mapper.RestaurantMessagingRequestDataMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;


@ApplicationScoped
@NoArgsConstructor
public class RestaurantApprovalRequestMessageListener extends RouteBuilder {
  @Override
  public void configure() {

    // stops module 07:60 Section -> implements and test RestaurantMessagingDataMapper

    // RestaurantApprovalRequestKafkaListener Implementation Sct::07::58
    // receives RestaurantApprovalRequestAvroModel / topic restaurant-approval-response
    // possible kafka:Consumer @KafkaListener receiving a RestaurantApprovalRequestAvroModel
    from("direct:approveOrder").routeId("RestaurantApprovalRequestMessageListenerRouteId")
            .bean(RestaurantMessagingRequestDataMapper::new) // return RestaurantApprovalRequest
            .to("direct:persistOrderApproval") // returns OrderApprovalEvent
            .log("persistOrderApproval Route returns ${body}")
            //.wireTap("seda:orderApprovalEventMessage")
            .wireTap("direct:orderApprovalEventMessage")
            .end();
  }
}
