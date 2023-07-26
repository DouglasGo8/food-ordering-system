package com.food.ordering.system.order.service.application.service.mapper;

import com.food.ordering.system.order.service.application.service.dto.message.PaymentResponseDTO;
import com.food.ordering.system.shared.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import javax.enterprise.context.ApplicationScoped;

@NoArgsConstructor
@ApplicationScoped
public class PaymentResponseDataMapper {

  @Handler
  public PaymentResponseDTO paymentResponseAvroModelToPaymentResponseDTO(@Body PaymentResponseAvroModel paymentResponseAvroModel) {

    return PaymentResponseDTO.builder()
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
