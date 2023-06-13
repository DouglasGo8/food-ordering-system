package com.food.ordering.system.payment.service.domain.core.event;

import com.food.ordering.system.payment.service.domain.core.entity.Payment;
import com.food.ordering.system.shared.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class PaymentEvent implements DomainEvent<Payment> {
  private final Payment payment;
  private final ZonedDateTime createdAt;
  private List<String> failureMessages;
}
