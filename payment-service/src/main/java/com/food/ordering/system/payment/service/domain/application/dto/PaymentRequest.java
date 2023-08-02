package com.food.ordering.system.payment.service.domain.application.dto;


import com.food.ordering.system.shared.domain.valueobject.PaymentOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class PaymentRequest {
  private String id;
  private String sagaId;
  private String orderId;
  private String customerId;
  //
  private BigDecimal price;
  private Instant createdAt;
  // needs organize shared project
  @Setter
  private PaymentOrderStatus paymentOrderStatus;
}
