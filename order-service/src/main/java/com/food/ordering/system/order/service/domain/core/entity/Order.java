package com.food.ordering.system.order.service.domain.core.entity;

import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.core.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.core.valueobject.TrackingId;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.entity.AggregateRoot;
import com.food.ordering.system.shared.domain.valueobject.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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


  private Order(Builder builder) {
    super.setId(builder.orderId);
    customerId = builder.customerId;
    restaurantId = builder.restaurantId;
    deliveryAddress = builder.deliveryAddress;
    price = builder.price;
    items = builder.items;
    trackingId = builder.trackingId;
    orderStatus = builder.orderStatus;
    failureMessages = builder.failureMessages;
  }

  public static Builder builder() {
    return new Builder();
  }

  //

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
      var messages = new ArrayList<String>();
      messages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
      //this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
      failureMessages = null;
      this.failureMessages = messages;
    }
    if (this.failureMessages == null) {
      this.failureMessages = failureMessages;
    }
  }


  public static final class Builder {
    private OrderId orderId;
    private Money price;

    private TrackingId trackingId;
    private CustomerId customerId;
    private OrderStatus orderStatus;
    private RestaurantId restaurantId;
    private StreetAddress deliveryAddress;
    //
    private List<OrderItem> items;
    private List<String> failureMessages;

    private Builder() {
    }

    public Builder price(Money price) {
      this.price = price;
      return this;
    }

    public Builder customerId(CustomerId customerId) {
      this.customerId = customerId;
      return this;
    }

    public Builder restaurantId(RestaurantId restaurantId) {
      this.restaurantId = restaurantId;
      return this;
    }

    public Builder deliveryAddress(StreetAddress deliveryAddress) {
      this.deliveryAddress = deliveryAddress;
      return this;
    }

    public Builder items(List<OrderItem> items) {
      this.items = items;
      return this;
    }

    public Builder trackingId(TrackingId trackingId) {
      this.trackingId = trackingId;
      return this;
    }

    public Builder orderStatus(OrderStatus orderStatus) {
      this.orderStatus = orderStatus;
      return this;
    }

    public Builder failureMessages(List<String> failureMessages) {
      this.failureMessages = failureMessages;
      return this;
    }

    public Builder orderId(OrderId id) {
      this.orderId = id;
      return this;
    }

    public Order build() {
     return new Order(this);
    }
  }
}