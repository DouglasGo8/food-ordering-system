package com.food.ordering.system.payment.service;

import com.food.ordering.system.payment.service.domain.core.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import io.quarkus.test.junit.QuarkusTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@QuarkusTest
public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  @Inject
  ProducerTemplate producerTemplate;

  @Inject
  PaymentDomainService paymentDomainService;

  // ***************
  // *** WARNING ***
  // ***************
  // Before tests
  // Truncate both tables TBL_CREDIT_ENTRY/TBL_CREDIT_HISTORY
  // after
  // Insert script.sql default values
  /*
    TRUNCATE TABLE TBL_PAYMENTS
    TRUNCATE TABLE TBL_CREDIT_ENTRY
    TRUNCATE TABLE TBL_CREDIT_HISTORY
    --
    SELECT * FROM TBL_PAYMENTS
    SELECT * FROM TBL_CREDIT_ENTRY
    SELECT * FROM TBL_CREDIT_HISTORY
   */
  @Test
  @Disabled
  @SneakyThrows
  public void persistPaymentRouteRepresentation() {

    AdviceWith.adviceWith(super.context, "PersistPaymentRouter", r -> r.weaveAddLast().to("mock:result"));
    //
    var body = this.createPaymentRequest();
    var mock = super.getMockEndpoint("mock:result");
    // from now, Invoked by RestEndpoint
    this.producerTemplate.sendBody("direct:completePayment", body);
    //
    mock.setExpectedMessageCount(1);
    //
    mock.assertIsSatisfied();

  }

  @Test
  @Disabled
  @SneakyThrows
  public void persistCancelPaymentRouteRepresentation() {
    AdviceWith.adviceWith(super.context, "PersistCancelPaymentRouter", r -> r.weaveAddLast().to("mock:result"));
    //
    var body = this.createPaymentRequest();
    // from now, Invoked by RestEndpoint
    this.producerTemplate.sendBody("direct:cancelPayment", body);
    var mock = super.getMockEndpoint("mock:result");
    //
    //
    mock.setExpectedMessageCount(1);
    //
    mock.assertIsSatisfied();
  }

  @Test
  @SneakyThrows
  public void paymentCompletedEvent() {
    //
    AdviceWith.adviceWith(super.context, "PaymentMessagePublisher", r -> r.weaveAddLast().to("mock:result"));
    //
    var payment = this.createPaymentWithPaymentIdtMock();
    var creditEntry = this.creditEntryByCustomerIdCamelJdbcMock();
    var creditHistories = this.creditHistoriesByCustomerIdCamelJdbcMock();
    // PaymentCompletedEvent
    var body = this.paymentDomainService.validateAndInitializePayment(payment, creditEntry, creditHistories);
    //
    this.producerTemplate.sendBody("direct:paymentMessagePublisher", body);
    var mock = super.getMockEndpoint("mock:result");
    //
    //body.getPayment().getPaymentStatus()
    mock.setExpectedMessageCount(1);
    //
    mock.assertIsSatisfied();

  }

  @Test
  @Disabled
  @SneakyThrows
  public void paymentCancelledEvent() {
    //
    AdviceWith.adviceWith(super.context, "PaymentMessagePublisher", r -> r.weaveAddLast().to("mock:result"));
    //
    var payment = this.createPaymentWithPaymentIdtMock();
    var creditEntry = this.creditEntryByCustomerIdCamelJdbcMock();
    var creditHistories = this.creditHistoriesByCustomerIdCamelJdbcMock();
    //
    var body = this.paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories);
    this.producerTemplate.sendBody("direct:paymentMessagePublisher", body);
    var mock = super.getMockEndpoint("mock:result");
    //
    mock.setExpectedMessageCount(1);
    //
    mock.assertIsSatisfied();
  }

  @Test
  @Disabled
  public void showTimedZone() {
    //log.info("time -> {}", ZonedDateTime.now());
/*    var payment = Payment.Builder

            .builder()
            .orderId(new OrderId(UUID.randomUUID()))
            .paymentStatus(PaymentStatus.COMPLETED)
            .price(new Money(BigDecimal.valueOf(22.30)))
            .createdAt(ZonedDateTime.now())
            .build();*/

    //log.info("{}",payment.getPaymentStatus());
    //log.info("{}", payment.getOrderId().getValue());
    //log.info("{}", payment.getCreatedAt());

    //var creditHistory = CreditHistory.builder()
    //        .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
    //        .build();

    //creditHistory.getId()

    log.info("{}", ZonedDateTime.now(ZoneId.systemDefault()).toOffsetDateTime().toZonedDateTime());


  }
}
