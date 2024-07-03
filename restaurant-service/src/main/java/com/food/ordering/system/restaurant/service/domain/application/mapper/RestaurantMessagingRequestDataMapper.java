package com.food.ordering.system.restaurant.service.domain.application.mapper;

import com.food.ordering.system.restaurant.service.domain.application.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.core.entity.Product;
import com.food.ordering.system.shared.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantOrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.Handler;

import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantMessagingRequestDataMapper {
  
  @Handler
  public RestaurantApprovalRequest
  restaurantApprovalRequestAvroModelToRestaurantApproval(RestaurantApprovalRequestAvroModel
                                                                 restaurantApprovalRequestAvroModel) {
    return RestaurantApprovalRequest.builder()
            .id(restaurantApprovalRequestAvroModel.getId())
            .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
            .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
            .orderId(restaurantApprovalRequestAvroModel.getOrderId())
            .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
                    .getRestaurantOrderStatus().name()))
            .products(restaurantApprovalRequestAvroModel.getProducts()
                    .stream().map(avroModel ->
                              Product.Builder.builder()
                                    .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                    .quantity(avroModel.getQuantity())
                                    .build())
                    .collect(Collectors.toList()))
            .price(restaurantApprovalRequestAvroModel.getPrice())
            .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
            .build();
  }
}
