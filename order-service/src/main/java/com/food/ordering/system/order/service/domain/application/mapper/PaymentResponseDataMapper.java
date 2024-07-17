package com.food.ordering.system.order.service.domain.application.mapper;

import com.food.ordering.system.order.service.domain.application.dto.message.PaymentResponse;
import com.food.ordering.system.shared.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import jakarta.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class PaymentResponseDataMapper {

  @Handler
  public PaymentResponse paymentResponseAvroModelToPaymentResponse(@Body PaymentResponseAvroModel paymentResponseAvroModel) {

    return PaymentResponse.builder()
            .id(paymentResponseAvroModel.getId())
            .sagaId(paymentResponseAvroModel.getSagaId())
            .paymentId(paymentResponseAvroModel.getPaymentId())
            .customerId(paymentResponseAvroModel.getCustomerId())
            .orderId(paymentResponseAvroModel.getOrderId())
            .price(paymentResponseAvroModel.getPrice())
            .createdAt(paymentResponseAvroModel.getCreatedAt())
            .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
            .failureMessages(paymentResponseAvroModel.getFailureMessages())
            .build();
  }
}
