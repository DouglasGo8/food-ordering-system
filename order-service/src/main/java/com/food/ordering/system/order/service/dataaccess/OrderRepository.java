package com.food.ordering.system.order.service.dataaccess;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class OrderRepository extends RouteBuilder {
  @Override
  public void configure() {

    from("direct:saveOrder").routeId("saveOrder")
            .setProperty("fail_msg", constant("")) // success saveOrder scenario
            .to("sql-stored:classpath:templates/insertOrders.sql") // saveOrder
            .end();

    // Removes code boilerplate from orderItemsToOrderItemEntities method
    from("direct:saveOrderItems").routeId("saveOrderItems")
            .split(simple("${body.items}")).streaming(true)
            //.parallelProcessing()
            .to("sql-stored:classpath:templates/insertOrderItems.sql")
            .end();


  }
}
