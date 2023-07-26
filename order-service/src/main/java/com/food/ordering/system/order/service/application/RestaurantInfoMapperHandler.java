package com.food.ordering.system.order.service.application;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantInfoMapperHandler extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    from("direct:createRestaurantInformationMapper").routeId("createRestaurantInformationMapper")
        .setHeader("ids", simple("${body.productsIdJoined}"))
            //.log("${body.restaurantId}")
        // work in Options ?options=resultType
        // sql:classpath:templates/findRestaurantsAndProductsInformation.sql
        .to("{{restaurantInfo.camel.sql.spEL}}") // ResultSetIterator
    .end();
  }
}
