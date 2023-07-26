package com.food.ordering.system.shared.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Money {

  private BigDecimal amount;
  public static final Money ZERO = new Money(BigDecimal.ZERO);

  public Money addMoney(Money money) {
    // add is a BigDecimal Operation
    return new Money(this.setScale(this.amount.add(money.amount)));
  }

  public Money subtractMoney(Money money) {
    return new Money(this.setScale(this.amount.subtract(money.amount)));
  }

  public Money multiplyMoney(int multiplier) {
    return new Money(this.setScale(this.amount.multiply(new BigDecimal(multiplier))));
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