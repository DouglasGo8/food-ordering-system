package com.food.ordering.system.restaurant.service.domain;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
@NoArgsConstructor
public class RestaurantApprovalRequestHelper extends RouteBuilder {


  // RestaurantDomainService
  // RestaurantDataMapper
  // RestaurantRepository
  // OrderApprovalRepository
  // OrderApproveMessagePublisher/Rejected

  // POSSIBLE ENTRY POINT
  @Override
  public void configure() throws Exception {

    // OrderApprovalEvent
    //from("direct:persistOrderApproval") // RestaurantApprovalRequest
    //        .log("Processing restaurant approval for order id. {}")
            // failure Messages creation as empty Array<String>
            // findRestaurant(RestaurantApprovalRequest) // returns Restaurant from RestaurantDataMapper class
            // restaurantRepository.findRestaurantInformation receives Restaurant
            // choice if restaurant was found
            // throws a new Exception RestaurantNotFoundException
            // RestaurantFromRepo .setActive(repo.isActive)
            // restaurantFromMapper.getProducts.forEach map restaurantFromRepo
            // restaurantDomainService.validateOrder(restaurant, failures messages, orderApprovalPub, orderApprovalRej) // OrderApprovalEVent
            // repo.save(restaurant.getOrderApproval)
   //         .end();
  }



}
