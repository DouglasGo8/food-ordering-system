package com.food.ordering.system.payment.service.domain.core.entity;


import com.food.ordering.system.payment.service.domain.core.valueobject.PaymentId;
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

  public Payment(Money price, PaymentId paymentId, OrderId orderId, CustomerId customerId) {
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
  }

  public void initializePayment() {
    super.setId(new PaymentId(UUID.randomUUID()));
    //
    this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
  }

  public void validatePayment(List<String> failureMessages) {
    if (price == null || !price.isGreaterThanZero()) {
      failureMessages.add("Total price must be greater than zero!!");
    }
  }

  public void updateStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

}

