package com.food.ordering.system.restaurant.service.domain.core.event;

import com.food.ordering.system.restaurant.service.domain.core.entity.OrderApproval;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {
  public OrderApprovedEvent(OrderApproval orderApproval, RestaurantId restaurantId,
                            List<String> failureMessages, ZonedDateTime createdAt) {
    super(orderApproval, restaurantId, failureMessages, createdAt);
  }

  // fire method will be implemented by Apache Camel Route
}
