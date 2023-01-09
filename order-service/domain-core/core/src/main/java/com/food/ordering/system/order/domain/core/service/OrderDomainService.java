package com.food.ordering.system.order.domain.core.service;

import com.food.ordering.system.order.domain.core.service.entity.Order;
import com.food.ordering.system.order.domain.core.service.entity.Restaurant;
import com.food.ordering.system.order.domain.core.service.event.OrderCancelledEvent;
import com.food.ordering.system.order.domain.core.service.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.core.service.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

  void approveOrder(Order order);

  void cancelOrder(Order order, List<String> failureMessages);

  OrderPaidEvent payOrder(Order order, List<String> failureMessages);

  OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

  OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);
}
