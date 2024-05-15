package com.food.ordering.system.restaurant.service.domain.core.event;

import com.food.ordering.system.restaurant.service.domain.core.entity.OrderApproval;
import com.food.ordering.system.shared.domain.event.DomainEvent;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class OrderApprovalEvent implements DomainEvent<OrderApproval> {
  private final OrderApproval orderApproval;
  private final RestaurantId restaurantId;
  private final List<String> failureMessages;
  private final ZonedDateTime createdAt;
}
