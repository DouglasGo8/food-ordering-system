package com.food.ordering.system.shared.order.core.service.domain.entity;

import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import lombok.Getter;

import java.util.List;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
  private boolean active;
  private final List<Product> products;

  public Restaurant(RestaurantId restaurantId, List<Product> products) {
    super.setId(restaurantId);
    this.products = products;
  }

  public Restaurant(RestaurantId restaurantId, boolean active, List<Product> products) {
    super.setId(restaurantId);
    //
    this.active = active;
    this.products = products;
  }
}
