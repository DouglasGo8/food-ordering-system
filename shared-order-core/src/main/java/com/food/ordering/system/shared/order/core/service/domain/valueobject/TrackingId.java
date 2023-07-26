package com.food.ordering.system.shared.order.core.service.domain.valueobject;

import com.food.ordering.system.shared.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
  public TrackingId(UUID value) {
    super(value);
  }
}