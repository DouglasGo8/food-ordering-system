package com.food.ordering.system.restaurant.service.domain.core.entity;

import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
public class OrderDetail extends BaseEntity<OrderId> {
  private Money totalAmount;
  private OrderStatus orderStatus;
  private final List<Product> products;

  private OrderDetail(Builder builder) {
    super.setId(builder.orderId);
    totalAmount = builder.totalAmount;
    orderStatus = builder.orderStatus;
    products = builder.products;
  }


  public static final class Builder {
    private OrderId orderId;
    private Money totalAmount;
    private OrderStatus orderStatus;
    private List<Product> products;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder orderId(OrderId orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder totalAmount(Money val) {
      totalAmount = val;
      return this;
    }

    public Builder orderStatus(OrderStatus val) {
      orderStatus = val;
      return this;
    }

    public Builder products(List<Product> val) {
      products = val;
      return this;
    }

    public OrderDetail build() {
      return new OrderDetail(this);
    }
  }
}
