package com.food.ordering.system.shared.order.core.service.domain.entity;

import com.food.ordering.system.shared.domain.entity.BaseEntity;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.order.core.service.domain.valueobject.OrderItemId;
import lombok.Getter;

@Getter
public class OrderItem extends BaseEntity<OrderItemId> {

  private OrderId orderId;
  //private OrderItemId orderItemId;
  private final Product product;
  private final Money price;
  private final Money subTotal;
  private final int quantity;

  public OrderItem(OrderId orderId, OrderItemId orderItemId, Product product, Money price, Money subTotal,
                   int quantity) {
    super.setId(orderItemId);
    this.price = price;
    this.orderId = orderId;
    this.product = product;
    this.subTotal = subTotal;
    this.quantity = quantity;
  }

  public OrderItem(OrderItemId orderItemId, Product product, Money price, Money subTotal, int quantity) {
    super.setId(orderItemId);
    this.price = price;
    this.product = product;
    this.subTotal = subTotal;
    this.quantity = quantity;
  }

  public OrderItem(Product product, Money price, Money subTotal, int quantity) {
    this.price = price;
    this.product = product;
    this.subTotal = subTotal;
    this.quantity = quantity;
  }

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

}
