package com.food.ordering.system.shared.order.core.service.domain;

import com.food.ordering.system.shared.order.core.service.domain.entity.Order;
import com.food.ordering.system.shared.order.core.service.domain.entity.Restaurant;
import com.food.ordering.system.shared.order.core.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.shared.order.core.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.shared.order.core.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
  void approveOrder(Order order);

  void cancelOrder(Order order, List<String> failuresMessages);

  OrderPaidEvent payOrder(Order order);

  OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

  OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

}
