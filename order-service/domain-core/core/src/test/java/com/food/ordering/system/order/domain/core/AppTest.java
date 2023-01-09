package com.food.ordering.system.order.domain.core;

import com.food.ordering.system.common.domain.valueobject.*;
import com.food.ordering.system.order.domain.core.service.entity.Order;
import com.food.ordering.system.order.domain.core.service.entity.OrderItem;
import com.food.ordering.system.order.domain.core.service.entity.Product;
import com.food.ordering.system.order.domain.core.service.valuobject.OrderItemId;
import com.food.ordering.system.order.domain.core.service.valuobject.StreetAddress;
import com.food.ordering.system.order.domain.core.service.valuobject.TrackingId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

  @Test
  public void createOrderInstance() {
    var money = new Money(BigDecimal.ONE);
    var orderId = new OrderId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());
    var trackingId = new TrackingId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var streetAddress = new StreetAddress(UUID.randomUUID(), "city", "postalCode", "streetName");
    var order = new Order(money, orderId, customerId, trackingId, OrderStatus.APPROVED,
            restaurantId, streetAddress, null, null);

    assertEquals(OrderStatus.APPROVED, order.getOrderStatus());
  }

  @Test
  public void createOrderItemInstance() {
    var money = new Money(BigDecimal.ONE);
    var productId = new ProductId(UUID.randomUUID());
    var product = new Product(money, "", productId);
    var orderItemId = new OrderItemId(10L);
    var orderId = new OrderId(UUID.randomUUID());
    var orderItem = new OrderItem(10, money, money, product, orderItemId, orderId);

    assertEquals(BigDecimal.ONE, orderItem.getPrice().amount());
  }

}
