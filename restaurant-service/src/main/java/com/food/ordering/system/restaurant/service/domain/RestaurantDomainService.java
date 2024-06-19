package com.food.ordering.system.restaurant.service.domain;


import com.food.ordering.system.restaurant.service.domain.core.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.core.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.core.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.core.event.OrderRejectedEvent;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Handler;
import org.apache.camel.Variable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class RestaurantDomainService {

  // params can change due to persistOrderApproval Router pipe
  @Handler
  OrderApprovalEvent validateOrder(@Variable("restaurant") Restaurant restaurant,
                                   @Variable("fail") List<String> failureMessages) {



    restaurant.validateOrder(failureMessages);


    log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());
    //
    if (failureMessages.isEmpty()) {
      log.info("Order is approved for order id: {}", restaurant.getOrderDetail().getId().getValue());
      restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
      //
      return new OrderApprovedEvent(restaurant.getOrderApproval(),
              restaurant.getId(), failureMessages,
              ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    }
    //
    restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
    //
    return new OrderRejectedEvent(restaurant.getOrderApproval(),
            restaurant.getId(), failureMessages,
            ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));

     //return null;
  }


}
