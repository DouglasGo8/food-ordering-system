package com.food.ordering.system.restaurant.service.domain.core;

import com.food.ordering.system.restaurant.service.domain.application.mapper.ValidateOrderMapper;
import com.food.ordering.system.restaurant.service.domain.core.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.core.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.core.event.OrderRejectedEvent;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.food.ordering.system.shared.domain.DomainConstants.UTC;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class RestaurantServiceImpl {

  @Handler
  public OrderApprovalEvent validateOrder(@Body ValidateOrderMapper mapper) {
    //
    var failures = mapper.failures();
    var restaurant = mapper.restaurant();
    //
    restaurant.validateOrder(failures);
    //
    if (failures.isEmpty()) {
      log.info("Order is approved for order id {}", restaurant.getOrderDetail().getId().getValue());
      restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
      //
      return new OrderApprovedEvent(restaurant.getOrderApproval(),
              restaurant.getId(), failures,
              ZonedDateTime.now(ZoneId.of(UTC)));
    }

    restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);

    return new OrderRejectedEvent(restaurant.getOrderApproval(),
            restaurant.getId(), failures,
            ZonedDateTime.now(ZoneId.of(UTC)));
  }
}
