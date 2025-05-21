package com.food.ordering.system.order.service.domain.core.entity;

import com.food.ordering.system.order.service.domain.core.valueobject.OrderItemId;
import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class OrderItem extends BaseEntity<OrderItemId> {

  private OrderId orderId;
  private final Product product;
  private final Money price;
  private final Money subTotal;
  private final int quantity;

  void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
    super.setId(orderItemId);
    //
    this.orderId = orderId;
  }

  public boolean isPriceValid() {

    return this.price.isGreaterThanZero() &&
            this.price.equals(this.product.getPrice()) &&
            this.price.multiplyMoney(quantity).equals(subTotal);
  }

  private OrderItem(Builder builder) {
    super.setId(builder.orderItemId);
    product = builder.product;
    quantity = builder.quantity;
    price = builder.price;
    subTotal = builder.subTotal;
  }

  public static Builder builder() {
    return new Builder();
  }


  public static final class Builder {
    private OrderItemId orderItemId;
    private Product product;
    private Money price;
    private Money subTotal;
    private int quantity;

    private Builder() {
    }

    public Builder orderItemId(OrderItemId orderItemId) {
      this.orderItemId = orderItemId;
      return this;
    }


    public Builder product(Product product) {
      this.product = product;
      return this;
    }

    public Builder price(Money price) {
      this.price = price;
      return this;
    }

    public Builder subTotal(Money subTotal) {
      this.subTotal = subTotal;
      return this;
    }

    public Builder quantity(int quantity) {
      this.quantity = quantity;
      return this;
    }

    public OrderItem build() {
      return new OrderItem(this);
    }
  }
}
