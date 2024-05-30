package com.food.ordering.system.restaurant.service.dataaccess.restaurant;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantRepository extends RouteBuilder {
  @Override
  public void configure() {

    // receives Restaurant
    //from("direct:findRestaurantInformation").routeId("RestaurantRepositoryRoute") // Optional<Restaurant>
    //        .log("")
    //        .end();
  }
}
