package com.food.ordering.system.order.service.domain.core.entity;

import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.core.valueobject.TrackingId;
import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.valueobject.*;
import lombok.Getter;

import java.util.List;

@Getter
public class Order extends AggregateRoot<OrderId> {

  private final Money price;
  private final CustomerId customerId;
  private final RestaurantId restaurantId;
  private final StreetAddress deliveryAddress;
  private final List<OrderItem> items;

  private TrackingId trackingId;
  private OrderStatus orderStatus;
  private List<String> failureMessages;

  public Order(OrderId orderId, Money price, CustomerId customerId, RestaurantId restaurantId,
               StreetAddress deliveryAddress, List<OrderItem> items) {
    //
    super.setId(orderId);
    //
    this.items = items;
    this.price = price;
    this.customerId = customerId;
    this.restaurantId = restaurantId;
    this.deliveryAddress = deliveryAddress;
  }

}
