package com.food.ordering.system.order.domain.core.service.event;

import com.food.ordering.system.common.domain.event.DomainEvent;
import com.food.ordering.system.order.domain.core.service.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public abstract class OrderEvent implements DomainEvent<Order> {
  private final Order order;
  private final ZonedDateTime createdAt;
}

