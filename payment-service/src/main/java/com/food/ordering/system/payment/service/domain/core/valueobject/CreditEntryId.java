package com.food.ordering.system.payment.service.domain.core.valueobject;

import com.food.ordering.system.shared.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {

  public CreditEntryId(UUID value) {
    super(value);
  }
}
