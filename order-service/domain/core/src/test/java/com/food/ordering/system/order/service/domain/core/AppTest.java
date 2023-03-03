package com.food.ordering.system.order.service.domain.core;

import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.shared.domain.valueobject.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author dougdb
 */
@Slf4j
public class AppTest {

  @Test
  public void streetAddressRepresentation() {
    var streetAddress = new StreetAddress(UUID.randomUUID(),
            "NY", "Avenue 5th 566", "122002");
    //
    assertEquals(streetAddress.city(), "NY");
  }

  @Test
  public void productRepresentation() {
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(4_299.32));
    var product = new Product(productId, "Mac Book M1 Max", price);
    assertEquals(productId.getValue(), product.getId().getValue());
  }

  @Test
  public void orderItemRepresentation() {
    var quantity = 2;
    var orderId = new OrderId(UUID.randomUUID());
    var orderItemId = new OrderItemId(12L);
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(4_299.32));
    var product = new Product(productId, "Mac Book M1 Max", price);
    //
    var subTotal = price.multiplyMoney(2);
    //
    var orderItem = new OrderItem(orderId, orderItemId,
            product, price, subTotal, quantity);

    //log.info("{}", orderItem.getSubTotal());
    assertNotNull(orderItem.getId());
    assertEquals(orderItem.getOrderId().getValue(), orderId.getValue());
    assertEquals(orderItem.getSubTotal().amount(), BigDecimal.valueOf(8598.64));

  }

  @Test
  public void orderRepresentation() {
    var order = this.initializerOrderMultipliedByTwoMock();
    //
    assertNotNull(order.getId());
    // later we'll use reduce to validate the same approach
    assertEquals(order.getItems().get(0).getSubTotal().amount(), BigDecimal.valueOf(8598.64));
    assertEquals(order.getRestaurantId().getValue(), order.getRestaurantId().getValue());
  }

  @Test
  public void initializerOrderRepresentation() {
    var order = initializerToValidateOrderInitiateMock();
    order.initializerOrder();
    //
    assertNotNull(order.getId());
    assertNotNull(order.getTrackingId());
    assertEquals(OrderStatus.PENDING, order.getOrderStatus());
    assertNotNull(order.getItems().get(0).getId());
    //
    order.validateOrder();

  }

  @Test
  public void statePayTransitionRepresentation() {
    var order = initializerToValidateOrderInitiateMock();
    order.initializerOrder();
    assertNotNull(order.getId());
    assertNotNull(order.getTrackingId());
    //
    order.pay();
    assertEquals(OrderStatus.PAID, order.getOrderStatus());
    //
    order.approve();
    assertEquals(OrderStatus.APPROVED, order.getOrderStatus());
    //

  }

  @Test
  public void stateInitCancelTransitionsRepresentation() {
    var order = initializerToValidateOrderInitiateMock();
    order.initializerOrder();
    assertNotNull(order.getId());
    assertNotNull(order.getTrackingId());
    //
    order.pay();
    assertEquals(OrderStatus.PAID, order.getOrderStatus());
    //
    var failedList = List.of("Not Approved");
    order.initCancel(failedList);
    assertEquals(1, order.getFailureMessages().size());
    assertEquals(OrderStatus.CANCELLING, order.getOrderStatus());
  }

  @Test
  public void restaurantRepresentation() {
    var productId = new ProductId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(4_299.32));
    var product = new Product(productId, "Mac Book M1 Max", price);
    var restaurant = new Restaurant(restaurantId, true, List.of(product));
    //
    assertEquals(restaurant.getId(), restaurantId);
    assertEquals(true, restaurant.isActive());
    assertEquals(1, restaurant.getProducts().size());
  }

  @Test
  public void orderDomainServiceCreatedEventRepresentation() {
    var order = this.initializerOrderWithStatusMock(OrderStatus.PENDING);
    //log.info("{}" ,order.getOrderStatus());
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(4_299.32));
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var product = new Product(productId, "Mac Book M1 Max", price);
    var restaurant = new Restaurant(restaurantId, true, List.of(product));
    var orderDomainService = new OrderDomainServiceImpl();
    // validateAndInitiateOrder
    var orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
    assertNotNull(orderCreatedEvent.getOrder().getId());
    assertEquals(orderCreatedEvent.getOrder().getOrderStatus(), OrderStatus.PENDING);
    //log.info(orderCreatedEvent.toString());
  }

  @Test
  public void orderDomainServiceApproveOrderRepresentation(){
    var order = this.initializerOrderWithStatusMock(OrderStatus.PAID);
    var orderEventApproved = new OrderDomainServiceImpl();
    orderEventApproved.approveOrder(order);
    assertEquals(order.getOrderStatus(), OrderStatus.APPROVED);
  }

  @Test
  public void orderDomainServiceCancelOrderRepresentation(){
    var order = this.initializerOrderWithStatusMock(OrderStatus.CANCELLING);
    var orderEventApproved = new OrderDomainServiceImpl();
    orderEventApproved.cancelOrder(order, List.of("Order Cancelled"));
    assertEquals(order.getOrderStatus(), OrderStatus.CANCELLED);
  }

  @Test
  public void orderDomainServicePayOrderRepresentation(){
    var order = this.initializerOrderWithStatusMock(OrderStatus.PENDING);
    var orderEventApproved = new OrderDomainServiceImpl();
    orderEventApproved.payOrder(order);
    assertEquals(order.getOrderStatus(), OrderStatus.PAID);
  }


  @Test
  public void moneyZeroRepresentation() {
    assertEquals(Money.ZERO.amount(), BigDecimal.ZERO);
  }

  @Test
  public void stringFormatPatternValidation() {
    var pattern = "This value %s is str %s";
    var value = BigDecimal.valueOf(30);
    var msg = UUID.randomUUID().toString();
    var result = String.format(pattern, value, msg);
    //
    assertTrue(result.contains("This value 30 is str"));
  }

  private Order initializerOrderWithStatusMock(OrderStatus orderStatus) {
    var quantity = 1;
    var orderId = new OrderId(UUID.randomUUID());
    var orderItemId = new OrderItemId(12L);
    var productId = new ProductId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(4_299.32));
    var product = new Product(productId, "Mac Book M1 Max", price);
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    var subTotal = price.multiplyMoney(quantity);

    //
    var orderItem = new OrderItem(orderId, orderItemId,
            product, price, subTotal, quantity);
    //
    return new Order(orderId, price, customerId, restaurantId,
            streetAddress, List.of(orderItem), orderStatus);
    //
  }

  private Order initializerOrderMultipliedByTwoMock() {
    var quantity = 2;
    var orderId = new OrderId(UUID.randomUUID());
    var orderItemId = new OrderItemId(12L);
    var productId = new ProductId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(4_299.32));
    var product = new Product(productId, "Mac Book M1 Max", price);
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    var subTotal = price.multiplyMoney(quantity);
    //
    var orderItem = new OrderItem(orderId, orderItemId,
            product, price, subTotal, quantity);
    //
    return new Order(orderId, price, customerId, restaurantId,
            streetAddress, List.of(orderItem));
    //
  }

  private Order initializerToValidateOrderInitiateMock() {
    //
    var quantity = 1;
    var orderItemId = new OrderItemId(1L);
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(4_299.32));
    //var price = new Money(BigDecimal.ZERO);
    var customerId = new CustomerId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var product = new Product(productId, "Mac Book M1 Max", price);
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    var subTotal = price.multiplyMoney(quantity);
    //var subTotal = price.multiplyMoney(2);
    //
    var orderItem = new OrderItem(orderItemId, product, price, subTotal, quantity);
    //
    return new Order(price, customerId, restaurantId, streetAddress, List.of(orderItem));
  }


}
