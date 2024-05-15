package com.food.ordering.system.restaurant.service.domain.core.valueobject;

import com.food.ordering.system.shared.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
  public OrderApprovalId(UUID value) {
    super(value);
  }
}
