package com.food.ordering.system.shared.order.core.service.domain.event;

import com.food.ordering.system.shared.domain.event.DomainEvent;
import com.food.ordering.system.shared.order.core.service.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public abstract class OrderEvent implements DomainEvent<Order> {
  private final Order order;
  private final ZonedDateTime createdAt;
}
