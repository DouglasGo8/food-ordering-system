package com.food.ordering.system.payment.service.domain.core.entity;

import com.food.ordering.system.payment.service.domain.core.valueobject.TransactionType;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import lombok.Getter;


@Getter
public class CreditHistory extends BaseEntity<CreditHistoryId> {
  private final Money amount;
  private final CustomerId customerId;
  private final TransactionType transactionType;

  public CreditHistory(Money amount, CreditHistoryId creditHistoryId, CustomerId customerId,
                       TransactionType transactionType) {
    super.setId(creditHistoryId);
    //
    this.amount = amount;
    this.customerId = customerId;
    this.transactionType = transactionType;

  }
}
