package com.food.ordering.system.order.service.dataaccess;

import com.food.ordering.system.shared.order.core.service.domain.exception.OrderDomainException;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantRepository extends RouteBuilder {

  @Override
  public void configure() {
    from("direct:checkRestaurantCommandHandler").routeId("checkRestaurantCMDH")
      .log(LoggingLevel.INFO,"finRestaurantByIdFunction invoked")
      // com.food.**.Restaurant@11053528
      .to("sql-stored:classpath:templates/finRestaurantByIdFunction.sql?function=true")
      .choice().when(simple("${body['#result-set-1'].size} == 0"))
        .log(LoggingLevel.INFO, "RestaurantId not found in here")
        .throwException(new OrderDomainException("Could not find Restaurant id"))
        //.otherwise()
        //.log(LoggingLevel.INFO, "Found RestaurantId")
      .end();

  }
}
