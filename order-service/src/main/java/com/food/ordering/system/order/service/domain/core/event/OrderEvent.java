package com.food.ordering.system.order.service.domain.core.event;


import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.shared.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public abstract class OrderEvent implements DomainEvent<Order> {
  private final Order order;
  private final ZonedDateTime createdAt;
}
