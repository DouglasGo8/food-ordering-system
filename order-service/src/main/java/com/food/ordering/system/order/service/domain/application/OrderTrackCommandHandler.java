package com.food.ordering.system.order.service.domain.application;

import com.food.ordering.system.order.service.domain.application.mapper.OrderDataMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class OrderTrackCommandHandler extends RouteBuilder {

  @Override
  public void configure() {

    // ExceptionHandler Concept
    //onException();

    from("direct:orderTrackCommandHandler").routeId("OrderTrackCommandHandlerRouteId")
            //.log("Message from ${body}")
            //.transform(simple("${header.uuid}"))
            .log(LoggingLevel.INFO, "trackingId - ${header.uuid}")
            .to("sql-stored:classpath:templates/findTrackingByIdFunction.sql?function=true") // returns Order
            // CamelSqlRow == 0 throws exception new OrderDomainNotFound("")
            //.marshal().json(JsonLibrary.Jackson)
            //.log(LoggingLevel.INFO, "trackingId - ${body['#result-set-1'][0]['tracking_id']}")
            .bean(OrderDataMapper.class, "{{trackingResponseJdbc.camel.method.spEL}}")
            .end();

  }

}
