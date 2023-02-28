package com.food.ordering.system.order.service.domain.core.entity;

import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import lombok.Getter;

import java.util.List;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
  private final boolean active;
  private final List<Product> products;

  public Restaurant(RestaurantId restaurantId, boolean active, List<Product> products) {
    super.setId(restaurantId);
    //
    this.active = active;
    this.products = products;
  }
}
