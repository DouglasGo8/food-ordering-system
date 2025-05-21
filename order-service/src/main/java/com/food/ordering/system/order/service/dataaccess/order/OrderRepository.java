package com.food.ordering.system.order.service.dataaccess.order;

import com.food.ordering.system.order.service.domain.core.exception.OrderNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class OrderRepository extends RouteBuilder {
  @Override
  public void configure() {

    from("direct:saveOrder").routeId("saveOrderRouteId")
            .setProperty("fail_msg", constant("")) // success saveOrder scenario
            //.log(LoggingLevel.INFO, "Creating Order saveOrder: ${body.restaurantId}")
            .to("sql-stored:classpath:templates/insertOrder.sql") // saveOrder
            .end();

    from("direct:saveOrderSaga").routeId("saveOrderSagaRouteId")
            .setProperty("fail_msg", constant("")) // success saveOrder scenario
            .to("sql-stored:classpath:templates/insertOrderSaga.sql") // saveOrder
            .end();

    // Removes code boilerplate from orderItemsToOrderItemEntities method
    from("direct:saveOrderItems").routeId("saveOrderItemsRouteId")
            .split(simple("${body.items}")).streaming(true)
            //.parallelProcessing()
            .to("sql-stored:classpath:templates/insertOrderItem.sql")
            .end();

    from("direct:findOrderAddressAndItemsById").routeId("findOrderAndAddressByIdRouteId")
            //.setVariable("orderId", simple("${body.orderId}"))
            .to("{{orderAddressItems.camel.sql.spEL}}")
            //.log("${body}")
            .choice().when(simple("${body} == null"))
              .log(LoggingLevel.ERROR, "Order with id: ${body.orderId} not be found")
              .throwException(new OrderNotFoundException("Order could not be found"))
            .end();
  }
}
