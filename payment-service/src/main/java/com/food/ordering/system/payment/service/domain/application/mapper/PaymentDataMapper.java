package com.food.ordering.system.payment.service.domain.application.mapper;


import com.food.ordering.system.payment.service.domain.application.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import lombok.NoArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@NoArgsConstructor
@ApplicationScoped
public class PaymentDataMapper {
  public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
    return Payment.builder()
            .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
            .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
            .price(new Money(paymentRequest.getPrice()))
            .build();
  }
}
