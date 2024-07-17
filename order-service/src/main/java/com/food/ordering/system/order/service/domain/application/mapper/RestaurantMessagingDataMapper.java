package com.food.ordering.system.order.service.domain.application.mapper;

import com.food.ordering.system.order.service.domain.application.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.event.OrderEvent;
import com.food.ordering.system.shared.avro.model.Product;
import com.food.ordering.system.shared.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.shared.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.shared.avro.model.RestaurantOrderStatus;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;

import lombok.NoArgsConstructor;
import org.apache.camel.Body;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantMessagingDataMapper {

  public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(@Body OrderEvent orderEvent) {
    var order = orderEvent.getOrder();
    final Function<List<OrderItem>, List<Product>> mapped = items ->
            items.stream().map(item -> new Product(item.getProduct().getId().getValue().toString(),
                    item.getQuantity())).collect(Collectors.toList());
    //
    return RestaurantApprovalRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setSagaId("")
            .setOrderId(order.getId().getValue().toString())
            .setRestaurantId(order.getRestaurantId().getValue().toString())
            .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
            .setProducts(mapped.apply(order.getItems()))
            .setPrice(order.getPrice().getAmount())
            .setCreatedAt(orderEvent.getCreatedAt().toInstant())
            .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
            .build();
  }

  public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(@Body RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
    return RestaurantApprovalResponse.builder()
            .id(restaurantApprovalResponseAvroModel.getId())
            .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
            .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
            .orderId(restaurantApprovalResponseAvroModel.getOrderId())
            .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
            .orderApprovalStatus(OrderApprovalStatus.valueOf(restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
            .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
            .build();
  }
}

