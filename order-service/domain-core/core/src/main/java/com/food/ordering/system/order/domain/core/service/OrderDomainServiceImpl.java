package com.food.ordering.system.order.domain.core.service;

import com.food.ordering.system.order.domain.core.service.entity.Order;
import com.food.ordering.system.order.domain.core.service.entity.Restaurant;
import com.food.ordering.system.order.domain.core.service.event.OrderCancelledEvent;
import com.food.ordering.system.order.domain.core.service.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.core.service.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.core.service.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {
  private static final String UTC = "UTC";

  @Override
  public void approveOrder(Order order) {
    order.approve();
    log.info("Order with id: {} is approved", order.getId().getValue());

  }

  @Override
  public void cancelOrder(Order order, List<String> failureMessages) {
    order.cancel(failureMessages);
    log.info("Order with id: {} is cancelled", order.getId().getValue());
  }

  @Override
  public OrderPaidEvent payOrder(Order order, List<String> failureMessages) {
    order.pay();
    log.info("Order with id: {} is paid", order.getId().getValue());
    return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  @Override
  public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
    order.initCancel(failureMessages);
    log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
    return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  @Override
  public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
    this.validateRestaurant(restaurant);
    this.setOrderProductInformation(order, restaurant);
    //
    order.validateOrder();
    order.initializeOrder();
    //
    log.info("Order with id: {} is initiated", order.getId().getValue());
    //
    return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  private void validateRestaurant(Restaurant restaurant) {
    if (!restaurant.isActive()) {
      throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
              " is currently not active!");
    }
  }

  private void setOrderProductInformation(Order order, Restaurant restaurant) {
    order.getOrderItems()
            .forEach(item -> restaurant.getProducts().forEach(rest -> {
              var product = item.getProduct();
              if (product.equals(rest)) {
                product.updateWithConfirmedNameAndPrice(rest.getName(), rest.getPrice());
              }
            }));
  }
}
