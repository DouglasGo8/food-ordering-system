package com.food.ordering.system.payment.service.domain.core.valueobject;

import com.food.ordering.system.shared.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {
  public PaymentId(UUID value) {
    super(value);
  }
}
