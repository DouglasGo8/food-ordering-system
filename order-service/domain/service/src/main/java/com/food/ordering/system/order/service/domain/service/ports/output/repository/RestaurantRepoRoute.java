package com.food.ordering.system.order.service.domain.service.ports.output.repository;

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
            // com.food.**.Restaurant@11053528
            .to("sql-stored:classpath:templates/getRestaurantByIdFunction.sql")
            .choice().when(simple("${header.CamelSqlRowCount} == 0"))
            .log(LoggingLevel.INFO, "RestaurantId not found in here")
            //.throwException(new OrderDomainException(OrderDomainInfo.RESTAURANT_ID_NOT_FOUND))
            .otherwise()
            .log(LoggingLevel.INFO, "${body}") // id is BaseId
            .end();
    //.log(LoggingLevel.INFO, "message from OrderCreateCommandHandler.class");

  }
}