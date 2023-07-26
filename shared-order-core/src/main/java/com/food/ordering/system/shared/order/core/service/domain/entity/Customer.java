package com.food.ordering.system.shared.order.core.service.domain.entity;

import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Customer extends AggregateRoot<CustomerId> {

  public Customer(CustomerId customerId) {
    super.setId(customerId);
  }
}