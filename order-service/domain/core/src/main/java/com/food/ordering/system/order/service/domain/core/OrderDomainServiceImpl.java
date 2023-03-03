package com.food.ordering.system.order.service.domain.core;

import com.food.ordering.system.order.service.domain.core.common.OrderDomainInfo;
import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.core.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.core.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


/**
 * Can use multiple Aggregates
 */
@Slf4j
@ApplicationScoped
public class OrderDomainServiceImpl implements OrderDomainService {
  @Override
  public void approveOrder(Order order) {
    order.approve();
    log.info("Order with id: {} is approved", order.getId().getValue());
  }

  @Override
  public void cancelOrder(Order order, List<String> failuresMessages) {
    order.cancel(failuresMessages);
    log.info("Order with id: {} is cancelled", order.getId().getValue());
  }

  @Override
  public OrderPaidEvent payOrder(Order order) {
    order.pay();
    log.info("Order with id: {} is paid", order.getId().getValue());
    return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(OrderDomainInfo.ZONED_OF_ID)));
  }

  @Override
  public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
    validateRestaurant(restaurant);
    setOrderProductInformation(order, restaurant);
    //
    order.validateOrder();
    order.initializerOrder();
    //
    log.info("Order with id: {}", order.getId());
    //
    return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(OrderDomainInfo.ZONED_OF_ID)));
  }

  @Override
  public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
    order.initCancel(failureMessages);
    log.info("Order payment is cancelling for order: {}", order.getId().getValue());
    return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(OrderDomainInfo.ZONED_OF_ID)));
  }

  private void validateRestaurant(Restaurant restaurant) {
    if (!restaurant.isActive()) {
      throw new OrderDomainException(OrderDomainInfo.RESTAURANT_STATE_INVALID);
    }
  }

  /**
   * Use Big O notation to reduce complexity
   * when using HashMap
   *
   * @param order
   * @param restaurant
   */
  private void setOrderProductInformation(Order order, Restaurant restaurant) {
    for (var item : order.getItems()) {
      for (var product : restaurant.getProducts()) {
        var currentProduct = item.getProduct();
        if (currentProduct.equals(product)) {
          currentProduct.updateWithConfirmedNameAndPrice(product.getName(), product.getPrice());
        }
      }
    }
  }
}
