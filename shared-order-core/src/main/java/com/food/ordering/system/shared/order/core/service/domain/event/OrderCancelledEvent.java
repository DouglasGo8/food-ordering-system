package com.food.ordering.system.shared.order.core.service.domain.event;

import com.food.ordering.system.shared.order.core.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {
  public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
    super(order, createdAt);
  }
  
}
