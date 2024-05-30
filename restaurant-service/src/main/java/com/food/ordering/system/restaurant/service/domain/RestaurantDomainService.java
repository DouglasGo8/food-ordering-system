package com.food.ordering.system.restaurant.service.domain;


import com.food.ordering.system.restaurant.service.domain.core.RestaurantServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantDomainService extends RouteBuilder {

  // direct:validateOrder
  //OrderApprovalEvent validateOrder(Restaurant restaurant,
  //                                 List<String> failureMessages
  //         /*OrderApprovedEvent implementation */);

  @Override
  public void configure() throws Exception {


    // ??? Is this route necessary ???
    //from("direct:validateOrder").routeId("RestaurantDomainServiceRoute") // receives ValidateOrderMapper
    //        .setVariable("orderId", simple("${body.restaurant.orderDetail.id.value}"))
            //.log("==> ${body.restaurant}")
    //        .log("Validating order with id: ${variable.orderId}")
    //        .bean(RestaurantServiceImpl::new) // returns OrderApprovalEvent
    //        .log("${body}")
            // .to(seda:orderDomainEventPublisher)
   //         .end();
  }


}
