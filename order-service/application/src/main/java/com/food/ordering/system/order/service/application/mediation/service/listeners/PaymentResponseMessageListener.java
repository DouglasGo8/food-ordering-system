package com.food.ordering.system.order.service.application.mediation.service.listeners;

import com.food.ordering.system.order.service.application.mediation.dto.message.PaymentResponseDTO;

public interface PaymentResponseMessageListener {
  void paymentCompleted(PaymentResponseDTO paymentResponse);
  void paymentCancelled(PaymentResponseDTO paymentResponse);
}
