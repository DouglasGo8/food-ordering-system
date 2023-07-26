package com.food.ordering.system.payment.service.domain.core.entity;

import com.food.ordering.system.payment.service.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.TransactionType;
import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import lombok.Getter;


@Getter
public class CreditHistory extends BaseEntity<CreditHistoryId> {
  private final Money amount;
  private final CustomerId customerId;
  private final TransactionType transactionType;

  private CreditHistory(Builder builder) {
    super.setId(builder.creditHistoryId);
    amount = builder.amount;
    customerId = builder.customerId;
    transactionType = builder.transactionType;

  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Money amount;
    private CustomerId customerId;
    private TransactionType transactionType;
    private CreditHistoryId creditHistoryId;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder amount(Money val) {
      amount = val;
      return this;
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder transactionType(TransactionType val) {
      transactionType = val;
      return this;
    }

    public Builder creditHistoryId(CreditHistoryId val) {
      this.creditHistoryId = val;
      return this;
    }

    public CreditHistory build() {
      return new CreditHistory(this);
    }
  }


}
