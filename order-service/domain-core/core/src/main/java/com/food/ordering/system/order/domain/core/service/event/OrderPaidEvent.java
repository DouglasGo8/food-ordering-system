package com.food.ordering.system.order.domain.core.service.event;


import com.food.ordering.system.order.domain.core.service.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {
  public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
    super(order, createdAt);
  }
}
