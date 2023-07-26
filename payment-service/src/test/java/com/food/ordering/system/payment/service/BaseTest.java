package com.food.ordering.system.payment.service;

import com.food.ordering.system.payment.service.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditEntryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.PaymentId;
import com.food.ordering.system.payment.service.domain.core.valueobject.TransactionType;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public interface BaseTest {
  default Payment createPaymentWithoutPaymentIdMock() {
    var price = new Money(BigDecimal.valueOf(12));
    var orderId = new OrderId(UUID.randomUUID());
    //var paymentId = new PaymentId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());
    //
    return Payment.Builder.builder()
            //.paymentId(paymentId)
            .price(price)
            .customerId(customerId)
            .orderId(orderId)
            .paymentStatus(PaymentStatus.COMPLETED)
            .build();
  }

  default Payment createPaymentWithPaymentIdtMock() {
    var price = new Money(BigDecimal.valueOf(12));
    var orderId = new OrderId(UUID.randomUUID());
    var paymentId = new PaymentId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());
    //
    return Payment.Builder.builder()
            .paymentId(paymentId)
            .price(price)
            .customerId(customerId)
            .orderId(orderId)
            .paymentStatus(PaymentStatus.COMPLETED)
            .build();
  }

  default CreditEntry createCreditEntryMock() {
    var money = new Money(BigDecimal.valueOf(20));
    var creditEntryId = new CreditEntryId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());

    return CreditEntry.builder()
            .creditEntryId(creditEntryId)
            .customerId(customerId)
            .totalCreditAmount(money)
            .build();
  }

  default CreditHistory createCreditHistory() {
    var money = new Money(BigDecimal.valueOf(20));
    var customerId = new CustomerId(UUID.randomUUID());
    var creditHistoryId = new CreditHistoryId(UUID.randomUUID());

    return CreditHistory.builder()
            .amount(money)
            .customerId(customerId)
            .creditHistoryId(creditHistoryId)
            .transactionType(TransactionType.CREDIT)
            .build();
  }
}
