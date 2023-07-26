package com.food.ordering.system.shared.order.core.service.domain.entity;

import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.valueobject.*;
import com.food.ordering.system.shared.order.core.service.domain.exception.OrderDomainException;
import com.food.ordering.system.shared.order.core.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.shared.order.core.service.domain.valueobject.TrackingId;
import com.food.ordering.system.shared.order.core.service.domain.valueobject.StreetAddress;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
public class Order extends AggregateRoot<OrderId> {

  //
  private final Money price;
  private final CustomerId customerId;
  private final RestaurantId restaurantId;
  private final StreetAddress deliveryAddress;
  //
  private final List<OrderItem> items;
  //

  private TrackingId trackingId;
  private OrderStatus orderStatus;
  private List<String> failureMessages;

  //
  public Order(OrderId orderId, Money price, CustomerId customerId,
               RestaurantId restaurantId, StreetAddress deliveryAddress, List<OrderItem> items) {
    //
    super.setId(orderId);
    //
    this.items = items;
    this.price = price;
    this.customerId = customerId;
    this.restaurantId = restaurantId;
    this.deliveryAddress = deliveryAddress;

  }

  public Order(OrderId orderId, Money price, CustomerId customerId, RestaurantId restaurantId,
               StreetAddress deliveryAddress, List<OrderItem> items, OrderStatus orderStatus) {
    //
    super.setId(orderId);
    //
    this.items = items;
    this.price = price;
    this.customerId = customerId;
    this.orderStatus = orderStatus;
    this.restaurantId = restaurantId;
    this.deliveryAddress = deliveryAddress;

  }

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
    this.setId(new OrderId(UUID.randomUUID()));
    // initial Order Status
    this.orderStatus = OrderStatus.PENDING;
    this.trackingId = new TrackingId(UUID.randomUUID());
    // other Methods
    this.initializerOrderItems();
  }

  public void validateOrder() {
    validateTotalPrice();
    validateItemsPrice();
    validateInitialOrder();
  }

  // ---- Order States Transitions BEGIN ----

  public void pay() {
    if (orderStatus != OrderStatus.PENDING) {
      throw new OrderDomainException(DomainConstants.ORDER_STATE_PAY_INVALID);
    }
    orderStatus = OrderStatus.PAID;
  }

  public void approve() {
    if (orderStatus != OrderStatus.PAID) {
      throw new OrderDomainException(DomainConstants.ORDER_STATE_APPROVE_INVALID);
    }
    orderStatus = OrderStatus.APPROVED;
  }

  public void initCancel(List<String> failureMessages) {
    if (orderStatus != OrderStatus.PAID) {
      throw new OrderDomainException(DomainConstants.ORDER_STATE_INIT_CANCEL_INVALID);
    }
    orderStatus = OrderStatus.CANCELLING;
    //
    updateFailureMessages(failureMessages);
  }

  public void cancel(List<String> failureMessages) {
    if (!(orderStatus == OrderStatus.CANCELLING ||
            orderStatus == OrderStatus.PENDING)) {
      throw new OrderDomainException(DomainConstants.ORDER_STATE_CANCEL_INVALID);
    }
    orderStatus = OrderStatus.CANCELLED;
    //
    updateFailureMessages(failureMessages);
  }

  // ---- Order States Transitions END ----

  private void initializerOrderItems() {
    long itemId = 1;
    //
    for (var item : items) {
      // item.initializeOrderItem(super.getId(), itemId++);
      //log.info(item.getId().toString());
      item.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
    }
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
      throw new OrderDomainException(String.format(DomainConstants.TOTAL_PRICE_INVALID_MSG, price.getAmount(),
              orderItemTotal.getAmount()));
    }
  }

  private void validateItemPrice(OrderItem orderItem) {
    if (!orderItem.isPriceValid()) {
      throw new OrderDomainException(String.format(DomainConstants.ORDER_ITEM_INVALID_PRICE,
              orderItem.getPrice().getAmount(),
              orderItem.getProduct().getId().getValue()));
    }
  }

  private void validateTotalPrice() {
    if (null == price || !price.isGreaterThanZero()) {
      throw new OrderDomainException(DomainConstants.INITIAL_PRICE_INVALID_MSG);
    }
  }

  private void validateInitialOrder() {

    if (orderStatus != null || this.getId() != null) {
      throw new OrderDomainException(DomainConstants.INITIAL_ORDER_INVALID_MSG);
    }
  }

  private void updateFailureMessages(List<String> failureMessages) {
    if (this.failureMessages != null && failureMessages != null) {
      this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
    }
    if (this.failureMessages == null) {
      this.failureMessages = failureMessages;
    }
  }


}