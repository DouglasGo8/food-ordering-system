package com.food.ordering.system.order.domain.core.service.entity;

import com.food.ordering.system.common.domain.entity.AggregateRoot;
import com.food.ordering.system.common.domain.valueobject.*;
import com.food.ordering.system.order.domain.core.service.exception.OrderDomainException;
import com.food.ordering.system.order.domain.core.service.valuobject.OrderItemId;
import com.food.ordering.system.order.domain.core.service.valuobject.StreetAddress;
import com.food.ordering.system.order.domain.core.service.valuobject.TrackingId;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Order extends AggregateRoot<OrderId> {

  private TrackingId trackingId;
  private OrderStatus orderStatus;

  private final Money price;
  private final CustomerId customerId;
  private final RestaurantId restaurantId;
  private final StreetAddress streetAddress;
  //
  private final List<OrderItem> orderItems;
  private final List<String> failureMessages;

  public Order(Money price, OrderId orderId, CustomerId customerId, TrackingId trackingId,
               OrderStatus orderStatus, RestaurantId restaurantId, StreetAddress streetAddress,
               List<OrderItem> orderItems, List<String> failureMessages) {
    //
    super.setId(orderId);
    //
    this.price = price;
    this.customerId = customerId;
    this.trackingId = trackingId;
    this.orderStatus = orderStatus;
    this.restaurantId = restaurantId;
    this.streetAddress = streetAddress;
    //
    this.orderItems = orderItems;
    this.failureMessages = failureMessages;
  }

  public void initializeOrder() {
    super.setId(new OrderId(UUID.randomUUID()));
    trackingId = new TrackingId(UUID.randomUUID());
    orderStatus = OrderStatus.PENDING;
    this.initializeOrderItems();
  }

  private void initializeOrderItems() {
    long itemId = 1;
    for (var orderItem : orderItems) {
      orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
    }
  }

  void validateOrder() {
    validateItemsPrice();
    validateTotalPrice();
    validateInitialOrder();
  }

  private void validateItemsPrice() {
    var orderItemsTotal = orderItems.stream()
            .filter(OrderItem::isPriceValid)
            .map(this::validateItemPrice)
            .reduce(Money.ZERO, Money::add);
    //
    if (price.equals(orderItemsTotal)) {
      throw new OrderDomainException("Total price: " + price.amount() +
              " is not Equals to Order items total..." + orderItemsTotal.amount() + " !");
    }
  }

  private Money validateItemPrice(OrderItem orderItem) {
    if (!orderItem.isPriceValid()) {
      throw new OrderDomainException("Order Item price " + orderItem.getPrice().amount() +
              " is not valid for current product " + orderItem.getProduct().getId().getValue());
    }
    return orderItem.getSubTotal();
  }

  private void validateTotalPrice() {
    if (price == null || !price.isGreaterThanZero()) {
      throw new OrderDomainException("Total price must be greater than zero");
    }
  }

  private void validateInitialOrder() {
    if (null != orderStatus || null != getId()) {
      throw new OrderDomainException("Order is not in correct state");
    }
  }

}

