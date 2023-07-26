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

  private CreditEntry(Builder builder) {
    super.setId(builder.creditEntryId);
    customerId = builder.customerId;
    totalCreditAmount = builder.totalCreditAmount;

  }

  public static Builder builder() {
    return new Builder();
  }

 /* public CreditEntry(CreditEntryId creditEntryId, CustomerId customerId, Money totalCreditAmount) {
    super.setId(creditEntryId);
    this.customerId = customerId;
    this.totalCreditAmount = totalCreditAmount;
    // total = ZERO
  }*/

  public void addCreditAmount(Money amount) {

    totalCreditAmount = totalCreditAmount.addMoney(amount);
  }

  public void subtractCreditAmount(Money amount) {
    this.totalCreditAmount = this.totalCreditAmount.subtractMoney(amount);
  }


  public static final class Builder {
    private CustomerId customerId;
    private Money totalCreditAmount;
    private CreditEntryId creditEntryId;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder totalCreditAmount(Money val) {
      totalCreditAmount = val;
      return this;
    }

    public Builder creditEntryId(CreditEntryId val) {
      creditEntryId = val;
      return this;
    }

    public CreditEntry build() {
      return new CreditEntry(this);
    }
  }
}
