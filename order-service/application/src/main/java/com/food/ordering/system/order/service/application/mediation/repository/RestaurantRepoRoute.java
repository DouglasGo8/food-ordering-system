package com.food.ordering.system.order.service.application.mediation.repository;

import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantRepoRoute extends RouteBuilder {
  @Override
  public void configure() {
    from("direct:checkRestaurantCommandHandler").routeId("checkRestaurantCMDH")
            //.log(LoggingLevel.INFO, "${body.restaurantId}")
            // com.food.**.Restaurant@11053528
            .to("sql-stored:classpath:templates/getRestaurantByIdFunction.sql?function=true")
            .choice().when(simple("${body['#result-set-1'].size} == 0"))
              .log(LoggingLevel.INFO, "RestaurantId not found in here")
            //.throwException(new OrderDomainException(OrderDomainInfo.RESTAURANT_ID_NOT_FOUND))
            .otherwise()
              .log(LoggingLevel.INFO, "Found RestaurantId")
            .end();

  }
}
