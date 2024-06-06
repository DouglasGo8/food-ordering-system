package com.food.ordering.system.restaurant.service.domain.application.mapper;

import com.food.ordering.system.restaurant.service.domain.application.dto.RestaurantApprovalRequest;


import com.food.ordering.system.restaurant.service.domain.core.entity.OrderDetail;
import com.food.ordering.system.restaurant.service.domain.core.entity.Product;
import com.food.ordering.system.restaurant.service.domain.core.entity.Restaurant;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@ApplicationScoped
public class RestaurantDataMapper {

  @Handler
  public Restaurant restaurantApprovalRequestModelToRestaurant(@Body RestaurantApprovalRequest
                                                                           restaurantApprovalRequest) {
    return Restaurant.Builder.builder()
            .restaurantId(new RestaurantId(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
            .orderDetail(OrderDetail.Builder
                    .builder()
                    .orderId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                    .products(restaurantApprovalRequest.getProducts().stream().map(
                            p -> Product.Builder.builder()
                                    .productId(p.getId())
                                    .quantity(p.getQuantity())
                                    .build()
                    ).collect(Collectors.toList()))
                    .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                    .orderStatus(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                    .build())
            .build();
  }
}
