package com.food.ordering.system.order.service.domain.core.event;


import com.food.ordering.system.order.service.domain.core.entity.Order;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {
  public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
    super(order, createdAt);
  }
}
