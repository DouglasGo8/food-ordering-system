package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.order.service.domain.core.event.OrderEvent;
import com.food.ordering.system.order.service.domain.core.event.OrderPaidEvent;
import com.food.ordering.system.shared.avro.model.PaymentOrderStatus;
import com.food.ordering.system.shared.avro.model.PaymentRequestAvroModel;

import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@NoArgsConstructor
@ApplicationScoped
public class OrderMessagingDataMapper {

  @Handler
  public PaymentRequestAvroModel orderEventToPaymentRequestAvroModel(@Body OrderEvent orderEvent) {

    var order = orderEvent.getOrder();
    //
    //var status = (orderEvent instanceof OrderCreatedEvent) ? PaymentOrderStatus.PENDING
     //       : PaymentOrderStatus.CANCELLED;
    //
    return PaymentRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setCustomerId(order.getCustomerId().getValue().toString())
            .setOrderId(order.getId().getValue().toString())
            .setPrice(order.getPrice().getAmount())
            .setCreatedAt(orderEvent.getCreatedAt().toInstant())
            .setPaymentOrderStatus(switch (orderEvent) {
              case OrderPaidEvent ignored -> PaymentOrderStatus.PENDING;
              default -> PaymentOrderStatus.CANCELLED;
            })
            .build();


  }


}

