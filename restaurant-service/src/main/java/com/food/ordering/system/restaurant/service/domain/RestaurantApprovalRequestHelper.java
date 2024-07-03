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

    // receives
    from("direct:persistOrderApproval").routeId("RestaurantApprovalRequestHelperRouteId") // RestaurantApprovalRequest
            .log(LoggingLevel.INFO, "Processing restaurant approval for Order Id. ${body.orderId}")
            .setVariable("orderId", simple("${body.orderId}"))
            .setVariable("payload", body())
            .setVariable("fail", simple("${empty(list)}")) // needs a test
            .bean(RestaurantDataMapper::new) //returns Restaurant from RestaurantDataMapper
            .setVariable("restaurant", body())
            //.log("${body}")
            .to("direct:findRestaurantInformation") // done
            .bean(RestaurantRepoMapper::new)
            .setVariable("restaurant", body())
            .bean(RestaurantDomainService::new) // returns OrderApprovalEvent
            //.setProperty("orderApprovalEvent", body())
            //.log("${body}")
            .wireTap("direct:saveOrderApproval")
            //.recipientList(constant("{{sendOrderApprovalEventCopy.spEL}}")).delimiter(";")
            //  .parallelProcessing()
            //.transform(exchangeProperty("orderApprovalEvent"))
            .end();
  }


}
