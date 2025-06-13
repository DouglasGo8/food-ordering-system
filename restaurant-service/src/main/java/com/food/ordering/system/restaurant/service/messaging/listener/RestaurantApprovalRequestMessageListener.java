package com.food.ordering.system.restaurant.service.messaging.listener;

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
    // receives RestaurantApprovalRequestAvroModel / topic restaurant-approval-request / Sct::08:65:7m:58s
    // kafka:Consumer @KafkaListener receiving a RestaurantApprovalRequestAvroModel
    from("kafka://{{restaurant.approval.topic.request}}"/*"direct:approveOrder")*/).routeId("RestaurantApprovalRequestMessageListenerRouteId")
            /*.log("Message received from Kafka (RestaurantApprovalRequestMessageListener_endpoint) : ${body}-${threadName}")
            .log("    on the topic ${headers[kafka.TOPIC]}")
            .log("    on the partition ${headers[kafka.PARTITION]}")
            .log("    with the offset ${headers[kafka.OFFSET]}")
            .log("    with the key ${headers[kafka.KEY]}")*/
            .bean(RestaurantMessagingRequestDataMapper::new) // return RestaurantApprovalRequest
            .to("direct:persistOrderApproval") // returns OrderApprovalEvent
            .end();
  }
}
