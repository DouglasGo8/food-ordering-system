package com.food.ordering.system.order.domain.application.service.ports.input.service.message.listener.payment;

import com.food.ordering.system.order.domain.application.service.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
  void paymentCompleted(PaymentResponse paymentResponse);
  void paymentCancelled(PaymentResponse paymentResponse);
}
