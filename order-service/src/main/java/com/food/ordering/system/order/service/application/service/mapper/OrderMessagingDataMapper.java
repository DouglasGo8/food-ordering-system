package com.food.ordering.system.order.service.application.service.mapper;

import com.food.ordering.system.shared.avro.model.PaymentOrderStatus;
import com.food.ordering.system.shared.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.shared.order.core.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.shared.order.core.service.domain.event.OrderEvent;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@NoArgsConstructor
@ApplicationScoped
public class OrderMessagingDataMapper {

  @Handler
  public PaymentRequestAvroModel orderEventToPaymentRequestAvroModel(@Body OrderEvent orderEvent) {

    var order = orderEvent.getOrder();
    //
    var status = (orderEvent instanceof OrderCreatedEvent) ? PaymentOrderStatus.PENDING
            : PaymentOrderStatus.CANCELLED;
    //
    return PaymentRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setCustomerId(order.getCustomerId().getValue().toString())
            .setOrderId(order.getId().getValue().toString())
            .setPrice(order.getPrice().getAmount())
            .setCreatedAt(orderEvent.getCreatedAt().toInstant())
            .setPaymentOrderStatus(status)
            .build();


  }


}

