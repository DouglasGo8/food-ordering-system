package com.food.ordering.system.payment.service.domain.application.mapper;

import com.food.ordering.system.payment.service.domain.application.dto.PaymentRequest;
import com.food.ordering.system.shared.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.shared.domain.valueobject.PaymentOrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.Handler;

@NoArgsConstructor
@ApplicationScoped
public class PaymentMessagingAvroRequestDataMapper {

  @Handler
  public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentResponseAvroModel) {

    return PaymentRequest.builder()
            .id(paymentResponseAvroModel.getId())
            .sagaId(paymentResponseAvroModel.getSagaId())
            .customerId(paymentResponseAvroModel.getCustomerId())
            .orderId(paymentResponseAvroModel.getOrderId())
            .price(paymentResponseAvroModel.getPrice())
            .createdAt(paymentResponseAvroModel.getCreatedAt())
            .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentResponseAvroModel.getPaymentOrderStatus().name()))
            .build();
  }
}
