package com.food.ordering.system.shared.order.core.service.domain.entity;

import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
//@EqualsAndHashCode(callSuper = false)
public class Product extends BaseEntity<ProductId> {
  private String name;
  private Money price;

  public Product(ProductId productId) {
    super.setId(productId);
  }

  public Product(ProductId productId, Money price) {
    super.setId(productId);
    this.price = price;
  }

  public Product(ProductId productId, String name, Money price) {
    super.setId(productId);
    //
    this.name = name;
    this.price = price;
  }

  public void updateWithConfirmedNameAndPrice(String name, Money price) {
    this.name = name;
    this.price = price;
  }
}