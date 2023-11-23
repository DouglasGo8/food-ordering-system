package com.food.ordering.system.payment.service;

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

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@QuarkusTest

public class CamelAppIT extends CamelQuarkusTestSupport implements BaseTest {

  @Inject
  ProducerTemplate producerTemplate;


  @Test
  @SneakyThrows
  public void persistPaymentRouteRepresentation() {

    AdviceWith.adviceWith(super.context, "PersistPaymentRouter", r -> r.weaveAddLast().to("mock:result"));
    //
    var body = this.createPaymentRequest();
    var mock = super.getMockEndpoint("mock:result");
    this.producerTemplate.sendBody("direct:persistPayment", body);
    //
    mock.setExpectedMessageCount(1);
    //
    mock.assertIsSatisfied();

  }

  @Test
  @Disabled
  @SneakyThrows
  public void persistCancelPaymentRouteRepresentation() {
    var body = this.createPaymentRequest();
    this.producerTemplate.sendBody("direct:persistCancelPayment", body);

    //
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
