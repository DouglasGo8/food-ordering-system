package com.food.ordering.system.restaurant.service.domain.application.mapper;

import com.food.ordering.system.restaurant.service.domain.core.event.OrderApprovalEvent;
import com.food.ordering.system.shared.avro.model.OrderApprovalStatus;
import com.food.ordering.system.shared.avro.model.RestaurantApprovalResponseAvroModel;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import java.util.UUID;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantMessagingResponseDataMapper {

  @Handler
  public RestaurantApprovalResponseAvroModel
  orderApprovedEventToRestaurantApprovalResponseAvroModel(@Body OrderApprovalEvent orderApprovalEvent) {
    //

    return RestaurantApprovalResponseAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setOrderId(orderApprovalEvent.getOrderApproval().getOrderId().getValue().toString())
            .setRestaurantId(orderApprovalEvent.getRestaurantId().getValue().toString())
            .setCreatedAt(orderApprovalEvent.getCreatedAt().toInstant())
            .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApprovalEvent.
                    getOrderApproval().getApprovalStatus().name()))
            .setFailureMessages(orderApprovalEvent.getFailureMessages())
            .build();
  }
}
