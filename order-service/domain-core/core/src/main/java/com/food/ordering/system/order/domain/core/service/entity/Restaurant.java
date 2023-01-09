package com.food.ordering.system.order.domain.core.service.entity;

import com.food.ordering.system.common.domain.entity.AggregateRoot;
import com.food.ordering.system.common.domain.valueobject.RestaurantId;
import lombok.Getter;

import java.util.List;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
  private final boolean isActive;
  private final List<Product>   products;

  public Restaurant(boolean isActive, List<Product> products, RestaurantId restaurantId) {
    super.setId(restaurantId);
    this.isActive = isActive;
    this.products = products;
  }
}
