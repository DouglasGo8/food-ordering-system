package com.food.ordering.system.order.service.application.mediation.repository;

import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
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
            .to("sql-stored:classpath:templates/finRestaurantByIdFunction.sql?function=true")
            .choice().when(simple("${body['#result-set-1'].size} == 0"))
              .log(LoggingLevel.INFO, "RestaurantId not found in here")
              .throwException(new OrderDomainException("Could not find Restaurant id"))
            .otherwise()
              .log(LoggingLevel.INFO, "Found RestaurantId")
            .end();


    from("direct:findRestaurantInformation").routeId("findRestaurantInformation")
            .setHeader("ids", simple("${body.productsIdJoined}"))
            // work in Options ?options=resultType
            .to("{{restaurantInfo.camel.sql.spEL}}") // ResultSetIterator

            .end();
  }
}
