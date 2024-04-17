package com.food.ordering.system.shared.domain.event;

public interface DomainEvent<T> {
  // is not necessary
  // camel will send the event to the Broker only based
  // in Payment interface
  //void fire();
}
