package com.food.ordering.system.order.service.messaging.consumer;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantApprovalResponseConsumerRoute extends RouteBuilder {
  @Override
  public void configure() {

    // from("kafka:topic?") RestaurantApprovalResponseAvroModel
    //.log("Message received from Kafka : ${body}-${threadName}")
    //.log("    on the topic ${headers[kafka.TOPIC]}")
    //.log("    on the partition ${headers[kafka.PARTITION]}")
    // .log("    with the offset ${headers[kafka.OFFSET]}")
    //.log("    with the key ${headers[kafka.KEY]}");
    // .choice .when(OrderApprovalStatus == APPROVED)
    // log("Processing approved order for order id: {}")
    // RestaurantApprovalResponseMessageListener.class approvalResponseAvroModelToApprovalResponse
    // when (OrderApprovalStatus == REJECTED)
    // log("Processing rejected for order id: {}, with fail messages

  }
}