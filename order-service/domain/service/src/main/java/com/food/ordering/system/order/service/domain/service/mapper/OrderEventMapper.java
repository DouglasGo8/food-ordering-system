package com.food.ordering.system.order.service.domain.service.mapper;


import com.food.ordering.system.shared.domain.event.DomainEventPublisher;

public class OrderEventMapper {

  void publishEvent(final DomainEventPublisher event) {
    // will be called from Camel Route
  }

}
