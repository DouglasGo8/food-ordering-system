package com.food.ordering.system.restaurant.service.domain.core.entity;

import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Product extends BaseEntity<ProductId> {

  private String name;
  private Money price;
  private final int quantity;
  private boolean available;

  private Product(Builder builder) {
    super.setId(builder.productId);

    name = builder.name;
    price = builder.price;
    quantity = builder.quantity;
    available = builder.available;
  }


  public void updateWithConfirmedPriceAndAvailable(String name, Money price, boolean available) {
    this.name = name;
    this.price = price;
    this.available = available;
  }


  public static final class Builder {
    private ProductId productId;
    private String name;
    private Money price;
    private int quantity;
    private boolean available;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder productId(ProductId productId) {
      this.productId = productId;
      return this;
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder price(Money val) {
      price = val;
      return this;
    }

    public Builder quantity(int val) {
      quantity = val;
      return this;
    }

    public Builder available(boolean val) {
      available = val;
      return this;
    }

    public Product build() {
      return new Product(this);
    }
  }
}
