package com.food.ordering.system.order.service.application.mediation.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantAndProductsInfo {
  private final String restaurantId;
  private final String productsIdJoined;
}
