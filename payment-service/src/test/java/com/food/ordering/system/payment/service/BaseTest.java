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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BaseTest {
  default Payment createPaymentWithoutPaymentIdMock() {
    var price = new Money(BigDecimal.valueOf(12.76d));
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

  default CreditEntry creditEntryByCustomerIdCamelJdbcMock() {

    // simulates camel jdbc return invoking a postgresql function

    /*return new ArrayList<>() {{
      add(Map.of
              (
                      "id",
                      "d215b5f8-0249-4dc5-89a3-51fd148cfb21",
                      "customer_id",
                      "af20558e-5e77-4a6e-bb2f-fef1f14c0ee9",
                      "total_credit_amount",
                      new BigDecimal("650.12")
              )
      );
    }}.get(0);*/


    /*var p =*/
    return CreditEntry.builder()
            .creditEntryId(new CreditEntryId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb21")))
            .customerId(new CustomerId(UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9")))
            .totalCreditAmount(new Money(new BigDecimal("650.12")))
            .build();

    //p.getTotalCreditAmount().getAmount()

  }

  default List<CreditHistory> creditHistoriesByCustomerIdCamelJdbcMock() {
    // simulates camel jdbc return invoking a postgresql function
    /*return new ArrayList<>() {{
      add(Map.of
              (
                      "id", "d215b5f8-0249-4dc5-89a3-51fd148cfb23",
                      "customer_id", "af20558e-5e77-4a6e-bb2f-fef1f14c0ee9",
                      "amount", new BigDecimal("100.00"),
                      "type", "CREDIT"
              )
      );
      add(Map.of
              (
                      "id", "d215b5f8-0249-4dc5-89a3-51fd148cfb25",
                      "customer_id", "af20558e-5e77-4a6e-bb2f-fef1f14c0ee9",
                      "amount", new BigDecimal("50.00"),
                      "type", "DEBIT"
              )
      );
      add(Map.of
              (
                      "id", "d215b5f8-0249-4dc5-89a3-51fd148cfb24",
                      "customer_id", "af20558e-5e77-4a6e-bb2f-fef1f14c0ee9",
                      "amount", new BigDecimal("600.12"),
                      "type", "CREDIT"
              )
      );
    }};*/
    return List.of(
            CreditHistory.builder()
                    .creditHistoryId(new CreditHistoryId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb23")))
                    .customerId(new CustomerId(UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9")))
                    .amount(new Money(new BigDecimal("100.00")))
                    .transactionType(TransactionType.CREDIT).build(),
            CreditHistory.builder()
                    .creditHistoryId(new CreditHistoryId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb25")))
                    .customerId(new CustomerId(UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9")))
                    .amount(new Money(new BigDecimal("50.00")))
                    .transactionType(TransactionType.DEBIT).build(),
            CreditHistory.builder()
                    .creditHistoryId(new CreditHistoryId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb24")))
                    .customerId(new CustomerId(UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9")))
                    .amount(new Money(new BigDecimal("600.12")))
                    .transactionType(TransactionType.CREDIT).build()
    );
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

    //
    return PaymentRequest.builder()
            .createdAt(Instant.now())
            //.createdAt(Instant.parse("2024-03-26T18:55:01.538321Z"))
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
