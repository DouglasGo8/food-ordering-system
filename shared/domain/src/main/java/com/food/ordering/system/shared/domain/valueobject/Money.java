package com.food.ordering.system.shared.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * records come with a default implementation
 * for hashCode(), equals() and toString() for all attributes inside the record
 *
 * @param amount
 */
public record Money(BigDecimal amount) {

  public Money addMoney(Money money) {
    // add is a BigDecimal Operation
    return new Money(this.setScale(this.amount.add(money.amount)));
  }

  public Money subtractMoney(Money money) {
    return new Money(this.setScale(this.amount.subtract(money.amount)));
  }

  public Money multiplyMoney(Money money) {
    return new Money(this.setScale(this.amount.multiply(money.amount)));
  }

  public BigDecimal setScale(BigDecimal input) {
    return input.setScale(2, RoundingMode.HALF_EVEN);
  }

  public boolean isGreaterThanZero() {
    return this.amount != null &&
            this.amount.compareTo(BigDecimal.ZERO) > 0;
  }

  public boolean isGreaterThan(Money money) {
    return this.amount != null &&
            this.amount.compareTo(money.amount) > 0;
  }


}
