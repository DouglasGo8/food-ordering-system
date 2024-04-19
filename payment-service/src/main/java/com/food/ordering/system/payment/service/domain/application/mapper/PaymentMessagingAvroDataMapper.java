package com.food.ordering.system.payment.service.domain.application.mapper;

import com.food.ordering.system.payment.service.domain.core.event.PaymentEvent;
import com.food.ordering.system.shared.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.shared.avro.model.PaymentStatus;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.Handler;

import java.util.UUID;

@NoArgsConstructor
@ApplicationScoped
public class PaymentMessagingAvroDataMapper {

  @Handler
  public PaymentResponseAvroModel paymentResponseAvroModel(PaymentEvent paymentEvent) {

    return PaymentResponseAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setPaymentId(paymentEvent.getPayment().getId().getValue().toString())
            .setCustomerId(paymentEvent.getPayment().getCustomerId().getValue().toString())
            .setOrderId(paymentEvent.getPayment().getOrderId().getValue().toString())
            .setPrice(paymentEvent.getPayment().getPrice().getAmount())
            .setCreatedAt(paymentEvent.getCreatedAt().toInstant())
            .setPaymentStatus(PaymentStatus.valueOf(paymentEvent.getPayment().getPaymentStatus().name()))
            .setFailureMessages(paymentEvent.getFailureMessages())
            .build();
  }


}
