package com.food.ordering.system.payment.service.domain.core.entity;

import com.food.ordering.system.payment.service.domain.core.valueobject.CreditEntryId;
import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import lombok.Getter;

@Getter
public class CreditEntry extends BaseEntity<CreditEntryId> {
  private final CustomerId customerId;
  //
  private Money totalCreditAmount;

  public CreditEntry(CreditEntryId creditEntryId, CustomerId customerId) {
    super.setId(creditEntryId);
    this.customerId = customerId;
    // total = ZERO
  }

  public void addCreditAmount(Money amount) {
    this.totalCreditAmount = totalCreditAmount.addMoney(amount);
  }

  public void subtractCreditAmount(Money amount) {
    this.totalCreditAmount = totalCreditAmount.subtractMoney(amount);
  }

}
