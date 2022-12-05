package com.food.ordering.system.order.domain.core.service.entity;

import com.food.ordering.system.common.domain.entity.BaseEntity;
import com.food.ordering.system.common.domain.valueobject.Money;
import com.food.ordering.system.common.domain.valueobject.ProductId;
import lombok.Getter;


@Getter
public class Product extends BaseEntity<ProductId> {
  private final Money price;
  private final String name;

  public Product(Money money, String name, ProductId productId) {
    super.setId(productId);
    this.name = name;
    this.price = money;
  }
}
