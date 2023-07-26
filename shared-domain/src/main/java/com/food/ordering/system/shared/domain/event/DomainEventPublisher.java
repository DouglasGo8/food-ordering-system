package com.food.ordering.system.shared.domain.event;

public interface DomainEventPublisher<T extends DomainEvent> {

  void publish(T domainEvent);
}