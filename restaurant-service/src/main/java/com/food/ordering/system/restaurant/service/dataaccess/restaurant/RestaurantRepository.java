package com.food.ordering.system.restaurant.service.dataaccess.restaurant;

import com.food.ordering.system.restaurant.service.domain.application.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.shared.order.core.service.domain.exception.OrderDomainException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantRepository extends RouteBuilder {
  @Override
  public void configure() {

    // receives Restaurant
    from("direct:findRestaurantInformation").routeId("RestaurantRepositoryRouteId")
            //.log("${body.orderDetail.products}")
            .setVariable("payload", body())
            .setHeader("ids", method(RestaurantDataAccessMapper.class, "{{joinedProductIds.camel.spEL}}"))
            //.log("${header.ids}")
            .to("{{restaurantInfo.camel.sql.spEL}}") // ResultSetIterator
            //.log("${body}")
            .choice().when(simple("${body.size} == 0"))
              .log(LoggingLevel.INFO, "RestaurantId ${variable.payload.id.value.toString} wasn't found")
              .throwException(new OrderDomainException("Could not find Restaurant"))
            .end();
  }
}
