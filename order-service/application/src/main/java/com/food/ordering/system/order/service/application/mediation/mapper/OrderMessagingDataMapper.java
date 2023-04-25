package com.food.ordering.system.order.service.application.mediation.mapper;

import com.food.ordering.system.order.service.domain.core.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.core.event.OrderEvent;
import com.food.ordering.system.shared.avro.model.PaymentOrderStatus;
import com.food.ordering.system.shared.avro.model.PaymentRequestAvroModel;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@ApplicationScoped
public class OrderMessagingDataMapper {

  @Handler
  public PaymentRequestAvroModel orderCreatedOrCancelledEventToPaymentRequestAvroModel(/*@Body OrderEvent orderEvent*/) {

    //var order = orderEvent.getOrder();
    //var status = (orderEvent instanceof OrderCreatedEvent) ? PaymentOrderStatus.PENDING
     //       : PaymentOrderStatus.CANCELLED;
    //
    /*return PaymentRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setCustomerId(order.getCustomerId().getValue().toString())
            .setOrderId(order.getId().getValue().toString())
            .setPrice(order.getPrice().getAmount())
            .setCreatedAt(orderEvent.getCreatedAt().toInstant())
            .setPaymentOrderStatus(status)
            .build();*/

    return PaymentRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setCustomerId(UUID.randomUUID().toString())
            .setOrderId(UUID.randomUUID().toString())
            .setPrice(BigDecimal.valueOf(2))
            .setCreatedAt(Instant.now())
            .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
            .build();
  }



}
