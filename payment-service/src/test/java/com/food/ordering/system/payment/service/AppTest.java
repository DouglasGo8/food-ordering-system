package com.food.ordering.system.payment.service;

import com.food.ordering.system.payment.service.domain.core.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentFailedEvent;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@QuarkusTest
public class AppTest implements BaseTest {

  @Inject
  PaymentDomainService paymentDomainService;


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

    var creditEntry = this.createCreditEntryMock();
    //new CreditEntry(creditEntryId, customerId, money);
    //

    assertNotNull(creditEntry.getId());
    creditEntry.addCreditAmount(new Money(BigDecimal.valueOf(10)));
    assertEquals(creditEntry.getTotalCreditAmount().getAmount().doubleValue(), 30.00);
    creditEntry.subtractCreditAmount(new Money(BigDecimal.valueOf(10)));
  }

  @Test
  public void creditHistoryRepresentation() {

    //var creditHistory = new CreditHistory(money, creditHistoryId, customerId, TransactionType.CREDIT);
    //
    var creditHistory = this.createCreditHistory();
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

  @Test
  public void validateAndInitializePaymentRepresentation() {
    var payment = this.createPaymentWithoutPaymentIdMock();
    var creditEntry = this.createCreditEntryMock();
    var creditHistory = List.of(this.createCreditHistory());
    var paymentEvent = this.paymentDomainService.validateAndInitializePayment(payment, creditEntry, creditHistory, List.of());
    assertNotNull(paymentEvent.getPayment().getId());
  }
}
