package com.food.ordering.system.payment.service;

import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.valueobject.PaymentId;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class AppTest {


  @Test
  public void simplePaymentPojoTest() {
    var price = new Money(BigDecimal.valueOf(12));
    var orderId = new OrderId(UUID.randomUUID());
    var paymentId = new PaymentId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());
    var payment = new Payment(price, paymentId, orderId, customerId,
            ZonedDateTime.now(), PaymentStatus.COMPLETED);

    log.info(payment.getPaymentStatus().toString());


    assertNotNull(payment.getId()
    );
    assertEquals(PaymentStatus.COMPLETED, payment.getPaymentStatus());

  }
}
