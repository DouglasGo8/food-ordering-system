package com.food.ordering.system.order.service.domain.application;

import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.core.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.core.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.shared.domain.DomainConstants;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.decorator.Decorator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Variable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@NoArgsConstructor
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
    return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
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
    return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
  }

  @Override
  public OrderCancelledEvent cancelOrderPayment(Order order, @Variable("failure_msg") List<String> failureMessages) {
    order.initCancel(failureMessages);
    log.info("Order payment is cancelling for order: {}", order.getId().getValue());
    return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
  }

  private void validateRestaurant(Restaurant restaurant) {
    if (!restaurant.isActive()) {
      throw new OrderDomainException(DomainConstants.RESTAURANT_STATE_INVALID);
    }
  }

  /**
   * Use Big O notation to reduce complexity,
   * e.g.; when using Map Or Stream
   *
   * @param order      param
   * @param restaurant param
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
