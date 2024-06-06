package com.food.ordering.system.restaurant.service.domain;


import com.food.ordering.system.restaurant.service.domain.application.mapper.RestaurantDataMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class RestaurantApprovalRequestHelper extends RouteBuilder {


  // RestaurantDomainService
  // RestaurantDataMapper
  // RestaurantRepository
  // OrderApprovalRepository
  // OrderApproveMessagePublisher/Rejected => OrderApprovalMessagePublisher::Router


  @Override
  public void configure() throws Exception {

    // OrderApprovalEvent
    from("direct:persistOrderApproval").routeId("RestaurantApprovalRequestHelperRouteId") // RestaurantApprovalRequest
            .log(LoggingLevel.INFO, "Processing restaurant approval for order id. ${body.orderId}")
            .setVariable("payload", body())
            .setVariable("failMessages", method(List.of())) // needs a test
            .bean(RestaurantDataMapper::new) //returns Restaurant from RestaurantDataMapper
            //.log("${body}")
            .to("direct:findRestaurantInformation") // done
            //.log("${body}")
            .setVariable("restaurantInfo", body())
            .transform(variable("payload"))
            // restaurantRepository.findRestaurantInformation receives Restaurant
            // choice if restaurant was found
            // throws a new Exception RestaurantNotFoundException
            // RestaurantFromRepo .setActive(repo.isActive)
            // restaurantFromMapper.getProducts.forEach map restaurantFromRepo
            // restaurantDomainService.validateOrder(restaurant, failures messages, orderApprovalPub, orderApprovalRej) // OrderApprovalEVent
            // repo.save(restaurant.getOrderApproval)
            .end();
  }


}
