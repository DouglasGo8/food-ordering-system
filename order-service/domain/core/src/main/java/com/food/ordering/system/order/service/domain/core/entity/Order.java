package com.food.ordering.system.order.service.domain.core.entity;

import com.food.ordering.system.order.service.domain.core.common.OrderDomainInfo;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.core.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.core.valueobject.TrackingId;
import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.exception.DomainException;
import com.food.ordering.system.shared.domain.valueobject.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @author dougdb
 */
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

  // needs use initializerOrder
  public Order(Money price, CustomerId customerId, RestaurantId restaurantId,
               StreetAddress deliveryAddress, List<OrderItem> items) {
    //
    this.items = items;
    this.price = price;
    this.customerId = customerId;
    this.restaurantId = restaurantId;
    this.deliveryAddress = deliveryAddress;
  }

  public void initializerOrder() {
    super.setId(new OrderId(UUID.randomUUID()));
    this.trackingId = new TrackingId(UUID.randomUUID());
    // initial Order Status
    this.orderStatus = OrderStatus.PENDING;
    // other Methods
    this.initializerOrderItems();

  }

  public void validateOrder() {
    validateTotalPrice();
    validateItemsPrice();
    validateInitialOrder();
  }

  private void validateItemsPrice() {

    //
    var orderItemTotal = this.items.stream()
            .map(orderItem -> {
              this.validateItemPrice(orderItem);
              return orderItem.getSubTotal();
            })
            .reduce(Money.ZERO, Money::addMoney);
    //
    if (!price.equals(orderItemTotal)) {
      throw new OrderDomainException(String.format(OrderDomainInfo.TOTAL_PRICE_INVALID_MSG,
              price.amount(), orderItemTotal.amount())
      );
    }
  }

  private void validateItemPrice(OrderItem orderItem) {
    if (!orderItem.isPriceValid()) {
      throw new OrderDomainException(OrderDomainInfo.ITEM_PRICE_INVALID_MSG);

    }
  }

  private void validateTotalPrice() {
    if (null == price || !price.isGreaterThanZero()) {
      throw new DomainException(OrderDomainInfo.INITIAL_PRICE_INVALID_MSG);
    }
  }

  private void validateInitialOrder() {
    if (null != orderStatus || null != super.getId()) {
      throw new OrderDomainException(OrderDomainInfo.INITIAL_ORDER_INVALID_MSG);
    }
  }

  private void initializerOrderItems() {
    long itemId = 1l;
    //
    for (var item : items) {
      item.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
    }
  }

}
