package com.food.ordering.system.order.service.domain.core.valueobject;

import com.food.ordering.system.shared.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
  public OrderItemId(Long value) {
    super(value);
  }
}
