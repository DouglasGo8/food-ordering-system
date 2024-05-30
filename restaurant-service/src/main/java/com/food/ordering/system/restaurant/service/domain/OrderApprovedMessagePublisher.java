package com.food.ordering.system.restaurant.service.domain;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class OrderApprovedMessagePublisher extends RouteBuilder {

  // will receive OrderApprovedEvent
  @Override
  public void configure() {

  }


}
