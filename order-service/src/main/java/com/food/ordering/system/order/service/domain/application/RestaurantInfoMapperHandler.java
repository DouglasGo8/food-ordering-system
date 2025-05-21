package com.food.ordering.system.order.service.domain.application;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantInfoMapperHandler extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    from("direct:createRestaurantInformationMapper").routeId("createRestaurantInformationMapperRouteId")
        .setHeader("ids", simple("${body.productsIdJoined}"))
            //.log(LoggingLevel.INFO, "${body.restaurantId}")
            //.log(LoggingLevel.INFO, "${header.ids}")
        // work in Options ?options=resultType
        // sql:classpath:templates/findRestaurantsAndProductsInformation.sql
        .to("{{restaurantInfo.camel.sql.spEL}}") // ResultSetIterator
    .end();
  }
}
