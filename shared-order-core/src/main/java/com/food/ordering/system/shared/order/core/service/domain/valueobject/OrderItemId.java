package com.food.ordering.system.shared.order.core.service.domain.valueobject;

import com.food.ordering.system.shared.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
  public OrderItemId(Long value) {
    super(value);
  }
}
