package com.food.ordering.system.restaurant.service.domain;


import com.food.ordering.system.restaurant.service.domain.application.mapper.RestaurantDataMapper;
import com.food.ordering.system.restaurant.service.domain.application.mapper.RestaurantRepoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
@NoArgsConstructor
public class RestaurantApprovalRequestHelper extends RouteBuilder {


  // RestaurantDomainService
  // RestaurantDataMapper
  // RestaurantRepository
  // OrderApprovalRepository
  // OrderApproveMessagePublisher/Rejected => OrderApprovalMessagePublisher::Router


  @Override
  public void configure() {

    // receives from OrderService Kafka topic and
    // send to same OrderService in restaurant-approval-response topic
    from("direct:persistOrderApproval").routeId("RestaurantApprovalRequestHelperRouteId") // RestaurantApprovalRequest
            .log(LoggingLevel.INFO, "Processing restaurant approval for Order Id. ${body.orderId}")
            .setVariable("orderId", simple("${body.orderId}"))
            .setVariable("payload", body())
            .setVariable("fail", simple("${empty(list)}"))
            // ---- Step 1: Bean RestaurantDataMapper Instance --------
            .bean(RestaurantDataMapper::new) //returns Restaurant from RestaurantDataMapper
            .setVariable("restaurant", body())
            .to("direct:findRestaurantInformation")
            // ---- Step 2: Bean RestaurantRepoMapper Instance ------
            .bean(RestaurantRepoMapper::new) // returns Restaurant validated
            // ----- Step 3: RestaurantDomainService Instance -------
            .bean(RestaurantDomainService::new) // returns OrderApprovalEvent
            .setProperty("orderApprovalEvent", body())
            // ------------------------------------------------------
            .recipientList(constant("{{sendOrderApprovalEvent.spEL}}"))
              .parallelProcessing()
              .stopOnException()
            .end();
  }


}
