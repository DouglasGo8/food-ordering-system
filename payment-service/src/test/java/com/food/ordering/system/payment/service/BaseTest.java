package com.food.ordering.system.payment.service;

import com.food.ordering.system.payment.service.domain.application.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditEntryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.PaymentId;
import com.food.ordering.system.payment.service.domain.core.valueobject.TransactionType;
import com.food.ordering.system.shared.domain.valueobject.*;

import java.math.BigDecimal;
import java.time.Instant;
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

  default PaymentRequest createPaymentRequest() {
    return PaymentRequest.builder()
            .createdAt(Instant.now())
            .customerId(UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9").toString())
            .id(UUID.randomUUID().toString())
            // must be a valid orderId
            .orderId("ec78b161-3899-4866-8753-886b84a8fbce")
            .paymentOrderStatus(PaymentOrderStatus.PENDING)
            .price(BigDecimal.valueOf(12.76d))
            .sagaId("")
            .build();
  }
}
