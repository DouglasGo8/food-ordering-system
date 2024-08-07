package com.food.ordering.system.payment.service;

import com.food.ordering.system.payment.service.domain.application.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.application.mapper.PaymentRequestDataMapper;
import com.food.ordering.system.payment.service.domain.core.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.core.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.core.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.core.exception.PaymentDomainException;
import com.food.ordering.system.payment.service.domain.core.exception.PaymentNotFoundException;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@QuarkusTest
public class AppTest implements BaseTest {

  @Inject
  PaymentRequestDataMapper paymentDataMapper;

  @Inject
  PaymentDomainService paymentDomainService;


  @Test
  @Disabled
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
  @Disabled
  public void simplePaymentPojoTest() {

    var payment = this.createPaymentWithoutPaymentIdMock();
    //new Payment(price, null, orderId, customerId, ZonedDateTime.now(),
    //PaymentStatus.COMPLETED);

    var failureMessages = new ArrayList<String>();
    //
    payment.initializePayment();
    payment.validatePayment(failureMessages);
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
  @Disabled
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
  @Disabled
  public void creditHistoryRepresentation() {

    //var creditHistory = new CreditHistory(money, creditHistoryId, customerId, TransactionType.CREDIT);
    //
    var creditHistory = this.createCreditHistory();
    assertNotNull(creditHistory.getId());
  }


  @Test
  @Disabled
  public void paymentCompletedEventRepresentation() {
    var paymentCompletedEvent = new PaymentCompletedEvent(this.createPaymentWithPaymentIdtMock(),
            ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    //
    assertNotNull(paymentCompletedEvent.getPayment().getId());
  }

  @Test
  @Disabled
  public void paymentCancelledEventRepresentation() {
    var paymentCancelledEvent = new PaymentCancelledEvent(this.createPaymentWithPaymentIdtMock(),
            ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    //
    assertNotNull(paymentCancelledEvent.getPayment().getId());
  }

  @Test
  @Disabled
  public void paymentFailedEventRepresentation() {
    var paymentFailedEvent = new PaymentFailedEvent(this.createPaymentWithPaymentIdtMock(),
            ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)), List.of());
    //
    assertNotNull(paymentFailedEvent.getPayment().getId());
  }

  @Test
  @Disabled
  public void validateAndInitializePaymentRepresentation() {
    var payment = this.createPaymentWithoutPaymentIdMock();
    // must simulate the returns of camel jdbc postgresql function invocation
    var creditEntries = this.creditEntryByCustomerIdCamelJdbcMock();
    var creditHistories = this.creditHistoriesByCustomerIdCamelJdbcMock();
    //
    var paymentEvent = this.paymentDomainService
            .validateAndInitializePayment(payment, creditEntries, creditHistories);
    //
    assertNotNull(paymentEvent.getPayment().getId());
    assertEquals(paymentEvent.getFailureMessages().size(), 0);
  }

  @Test
  @Disabled
  public void validateAndCancelPaymentRepresentation() {
    var payment = this.createPaymentWithoutPaymentIdMock();
    var creditEntries = this.creditEntryByCustomerIdCamelJdbcMock();
    var creditHistories = this.creditHistoriesByCustomerIdCamelJdbcMock();
    //
    var paymentEvent = this.paymentDomainService
            .validateAndCancelPayment(payment, creditEntries, creditHistories);
    //
    assertEquals(paymentEvent.getPayment().getPaymentStatus(), PaymentStatus.CANCELLED);
    assertEquals(paymentEvent.getFailureMessages().size(), 0);
  }

  @Test
  @Disabled
  public void paymentDataMapperRequestModelToPaymentRepresentation() {
    // paymentRequest
    var paymentRequest = PaymentRequest.builder()
            .id(UUID.randomUUID().toString())
            .orderId(UUID.randomUUID().toString())
            .customerId(UUID.randomUUID().toString())
            .price(BigDecimal.valueOf(22.02))
            .createdAt(Instant.now())
            .paymentOrderStatus(PaymentOrderStatus.PENDING)
            .build();
    //
    var mapper = this.paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
    log.info("{}", mapper);
  }

  @Test
  @Disabled
  public void paymentDomainExceptionRepresentation() {
    assertThrows(PaymentDomainException.class, () -> {
      throw new PaymentDomainException("Some PaymentDomainException occurs");
    });
  }

  @Test
  @Disabled
  public void paymentApplicationExceptionRepresentation() {
    assertThrows(PaymentApplicationServiceException.class, () -> {
      throw new PaymentApplicationServiceException("Some PaymentApplicationException occurs");
    });
  }

  @Test
  @Disabled
  public void paymentNotFoundExceptionRepresentation() {
    assertThrows(PaymentNotFoundException.class, () -> {
      throw new PaymentNotFoundException("Some PaymentNotFoundException occurs");
    });
  }
}
