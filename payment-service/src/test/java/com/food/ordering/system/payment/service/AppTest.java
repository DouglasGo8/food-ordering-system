package com.food.ordering.system.payment.service;

import com.food.ordering.system.payment.service.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditEntryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.core.valueobject.TransactionType;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class AppTest implements BaseTest {


  @Test

  public void simplePaymentPojoTest() {

    var payment = this.createPaymentWithoutPaymentIdMock();
    //new Payment(price, null, orderId, customerId, ZonedDateTime.now(),
    //PaymentStatus.COMPLETED);

    //
    payment.initializePayment();
    payment.validatePayment(List.of());
    //
    log.info(payment.getPaymentStatus().toString());
    //
    assertNotNull(payment.getId());
    assertEquals(PaymentStatus.COMPLETED, payment.getPaymentStatus());
    //
    payment.updateStatus(PaymentStatus.CANCELLED);
    assertEquals(PaymentStatus.CANCELLED, payment.getPaymentStatus());

  }

  @Test
  public void creditEntryRepresentation() {
    var money = new Money(BigDecimal.valueOf(20));
    var creditEntryId = new CreditEntryId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());
    var creditEntry = CreditEntry.builder()
            .creditEntryId(creditEntryId)
            .customerId(customerId)
            .totalCreditAmount(money)
            .build();
    //new CreditEntry(creditEntryId, customerId, money);
    //

    assertNotNull(creditEntry.getId());
    creditEntry.addCreditAmount(new Money(BigDecimal.valueOf(10)));
    assertEquals(creditEntry.getTotalCreditAmount().getAmount().doubleValue(), 30.00);
    creditEntry.subtractCreditAmount(new Money(BigDecimal.valueOf(10)));
    assertEquals(creditEntry.getTotalCreditAmount().getAmount().doubleValue(), money.getAmount().doubleValue());
  }

  @Test
  public void creditHistoryRepresentation() {
    var money = new Money(BigDecimal.valueOf(20));
    var customerId = new CustomerId(UUID.randomUUID());
    var creditHistoryId = new CreditHistoryId(UUID.randomUUID());
    //var creditHistory = new CreditHistory(money, creditHistoryId, customerId, TransactionType.CREDIT);
    //

    var creditHistory = CreditHistory.builder()
            .amount(money)
            .customerId(customerId)
            .creditHistoryId(creditHistoryId)
            .transactionType(TransactionType.CREDIT)
            .build();

    assertNotNull(creditHistory.getId());
  }


  @Test
  public void paymentCompletedEventRepresentation() {
    var paymentCompletedEvent = new PaymentCompletedEvent(this.createPaymentWithPaymentIdtMock(),
            ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    //
    assertNotNull(paymentCompletedEvent.getPayment().getId());
  }

  @Test
  public void paymentCancelledEventRepresentation() {
    var paymentCancelledEvent = new PaymentCancelledEvent(this.createPaymentWithPaymentIdtMock(),
            ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    //
    assertNotNull(paymentCancelledEvent.getPayment().getId());
  }

  @Test
  public void paymentFailedEventRepresentation() {
    var paymentFailedEvent = new PaymentFailedEvent(this.createPaymentWithPaymentIdtMock(),
            ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)), List.of());
    //
    assertNotNull(paymentFailedEvent.getPayment().getId());
  }
}
