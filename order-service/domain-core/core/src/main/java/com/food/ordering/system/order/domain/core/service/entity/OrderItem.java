package com.food.ordering.system.order.domain.core.service.entity;

import com.food.ordering.system.common.domain.entity.BaseEntity;
import com.food.ordering.system.common.domain.valueobject.Money;
import com.food.ordering.system.common.domain.valueobject.OrderId;
import com.food.ordering.system.order.domain.core.service.valuobject.OrderItemId;
import lombok.Getter;

@Getter
public class OrderItem extends BaseEntity<OrderItemId> {

  private final int quantity;
  private final Money price;
  private final Money subTotal;
  private final Product product;
  private OrderId orderId;
  //

  /**
   * @param quantity    of
   * @param price       of
   * @param subTotal    of
   * @param product     of
   * @param orderItemId of
   * @param orderId     of
   */
  public OrderItem(int quantity, Money price, Money subTotal, Product product,
                   OrderItemId orderItemId, OrderId orderId) {
    super.setId(orderItemId);
    this.price = price;
    this.orderId = orderId;
    this.product = product;
    this.quantity = quantity;
    this.subTotal = subTotal;
  }


  protected void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
    super.setId(orderItemId);
    //
    this.orderId = orderId;
  }

  protected boolean isPriceValid() {
    return this.price.isGreaterThanZero() &&
            price.equals(product.getPrice()) &&
            price.multiply(quantity).equals(subTotal);
  }
}
