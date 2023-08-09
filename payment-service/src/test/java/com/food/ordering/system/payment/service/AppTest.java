package com.food.ordering.system.payment.service;

import com.food.ordering.system.payment.service.domain.application.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.application.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentFailedEvent;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@QuarkusTest
public class AppTest implements BaseTest {

  @Inject
  PaymentDataMapper paymentDataMapper;

  @Inject
  PaymentDomainService paymentDomainService;


  @Test
  public void paymentRequestRepresentation() {
    var paymentRequest = PaymentRequest.builder()
            .id(UUID.randomUUID().toString())
            .orderId(UUID.randomUUID().toString())
            .price(BigDecimal.valueOf(10))
            .customerId(UUID.randomUUID().toString())
            .createdAt(Instant.now())
            .sagaId("")
            .paymentOrderStatus(PaymentOrderStatus.PENDING)
            .build();


    assertNotNull(paymentRequest.getId());
    assertEquals(paymentRequest.getPaymentOrderStatus(), PaymentOrderStatus.PENDING);
  }

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
    List<CreditHistory> creditHistory = new ArrayList<>();
    creditHistory.add(this.createCreditHistory());
    // Collections.singletonList(this.createCreditHistory());// List.of(this.createCreditHistory());
    var paymentEvent = this.paymentDomainService
            .validateAndInitializePayment(payment, creditEntry, creditHistory, List.of());
    //
    assertNotNull(paymentEvent.getPayment().getId());
    assertEquals(paymentEvent.getFailureMessages().size(), 0);
  }

  @Test
  public void validateAndCancelPaymentRepresentation() {
    var payment = this.createPaymentWithoutPaymentIdMock();
    var creditEntry = this.createCreditEntryMock();
    List<CreditHistory> creditHistory = new ArrayList<>();
    creditHistory.add(this.createCreditHistory());
    var paymentEvent = this.paymentDomainService
            .validateAndCancelPayment(payment, creditEntry, creditHistory, List.of());
    assertEquals(paymentEvent.getPayment().getPaymentStatus(), PaymentStatus.CANCELLED);
    assertEquals(paymentEvent.getFailureMessages().size(), 0);
  }

  @Test
  public void paymentDataMapperRequestModelToPaymentRepresentation() {
    // paymentRequest
    //this.paymentDataMapper.paymentRequestModelToPayment(null)
  }
}
