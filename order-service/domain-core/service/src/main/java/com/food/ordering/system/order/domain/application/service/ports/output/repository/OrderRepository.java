package com.food.ordering.system.order.domain.application.service.ports.output.repository;

import com.food.ordering.system.order.domain.core.service.entity.Order;
import com.food.ordering.system.order.domain.core.service.valuobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {

  Order save(Order order);
  Optional<Order> findByTrackingId(TrackingId trackingId);
}
