package com.food.ordering.system.payment.service.domain.core.entity;


import com.food.ordering.system.payment.service.domain.core.valueobject.PaymentId;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class Payment extends AggregateRoot<PaymentId> {

  private final Money price;
  private final OrderId orderId;
  private final CustomerId customerId;

  //
  private ZonedDateTime createdAt;
  private PaymentStatus paymentStatus;

  private Payment(Builder builder) {
    super.setId(builder.paymentId);
    //
    price = builder.price;
    orderId = builder.orderId;
    customerId = builder.customerId;
    createdAt = builder.createdAt;
    paymentStatus = builder.paymentStatus;

  }

  public static Builder builder() {
    return new Builder();
  }


  /*public Payment(Money price, PaymentId paymentId, OrderId orderId, CustomerId customerId) {
    super.setId(paymentId);
    this.price = price;
    this.orderId = orderId;
    this.customerId = customerId;
  }


  public Payment(Money price, PaymentId paymentId, OrderId orderId, CustomerId customerId,
                 ZonedDateTime createdAt,
                 PaymentStatus paymentStatus) {
    super.setId(paymentId);
    this.price = price;
    this.orderId = orderId;
    this.customerId = customerId;
    //
    this.createdAt = createdAt;
    this.paymentStatus = paymentStatus;
  }*/

  public void initializePayment() {
    super.setId(new PaymentId(UUID.randomUUID()));
    //
    this.createdAt = ZonedDateTime.now(ZoneId.of(DomainConstants.UTC));
  }

  public void validatePayment(List<String> failureMessages) {
    if (price == null || !price.isGreaterThanZero()) {
      failureMessages.add("Total price must be greater than zero!!");
    }
  }

  public void updateStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }


  public static final class Builder {
    private Money price;
    private OrderId orderId;
    private CustomerId customerId;
    private ZonedDateTime createdAt;
    private PaymentStatus paymentStatus;
    private PaymentId paymentId;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder price(Money val) {
      price = val;
      return this;
    }

    public Builder orderId(OrderId val) {
      orderId = val;
      return this;
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder createdAt(ZonedDateTime val) {
      createdAt = val;
      return this;
    }

    public Builder paymentStatus(PaymentStatus val) {
      paymentStatus = val;
      return this;
    }

    public Builder paymentId(PaymentId val) {
      paymentId = val;
      return this;
    }

    public Payment build() {
      return new Payment(this);
    }
  }
}

