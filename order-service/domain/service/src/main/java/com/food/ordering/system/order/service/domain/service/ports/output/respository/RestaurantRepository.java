package com.food.ordering.system.order.service.domain.service.ports.output.respository;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantRepository extends RouteBuilder {
  @Override
  public void configure() {
    // direct:findRestaurantInformation(Restaurant restaurant)/Jdbc Component in Postgres Function returning Optional<Restaurant>
  }
}
