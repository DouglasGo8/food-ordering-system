package com.food.ordering.system.restaurant.service.domain.application.dto;

import com.food.ordering.system.restaurant.service.domain.core.entity.Product;
import com.food.ordering.system.shared.domain.valueobject.RestaurantOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalRequest {
  private String id;
  private String sagaId;
  private String orderId;
  private String restaurantId;
  private RestaurantOrderStatus restaurantOrderStatus;
  //
  private BigDecimal price;
  private Instant createdAt;
  private List<Product> products;

}
