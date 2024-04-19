package com.food.ordering.system.payment.service.domain.application.mapper;


import com.food.ordering.system.payment.service.domain.application.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.payment.service.domain.core.valueobject.PaymentId;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@ApplicationScoped
public class PaymentRequestDataMapper {
  @Handler
  public Payment paymentRequestModelToPayment(@Body PaymentRequest paymentRequest) {
    return Payment.builder()
            .paymentId(new PaymentId(UUID.randomUUID()))
            .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
            .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
            .price(new Money(paymentRequest.getPrice()))
            .createdAt(ZonedDateTime.now(ZoneId.systemDefault()).toOffsetDateTime().toZonedDateTime())
            .paymentStatus(PaymentStatus.COMPLETED)
            .build();
  }
}
