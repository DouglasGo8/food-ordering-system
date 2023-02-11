package com.food.ordering.system.order.service.domain.core;

import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.core.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.core.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

  void approveOrder(Order order);
  void cancelOrder(Order order, List<String> failuresMessages);

  OrderPaidEvent payOrder(Order order);

  OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

  OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);



}
