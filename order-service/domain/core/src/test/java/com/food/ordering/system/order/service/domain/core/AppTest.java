package com.food.ordering.system.order.service.domain.core;

import com.food.ordering.system.order.service.domain.core.common.OrderDomainInfo;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.core.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.OrderId;
import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author dougdb
 */
@Slf4j
@QuarkusTest
public class AppTest extends BaseTest {

  @Inject
  OrderDomainService orderDomainService;

  @Test
  public void streetAddressRepresentation() {
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    assertNotNull(streetAddress.id());
    assertEquals(streetAddress.city(), "NY");
  }

  @Test
  public void productRepresentation() {
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(9.32));
    var product = new Product(productId, "Minced Beef", price);
    //
    assertNotNull(product.getId());
    assertEquals(productId.getValue(), product.getId().getValue());
  }

  @Test
  public void restaurantRepresentation() {
    var restaurant = super.restaurantInitiateMock();
    //
    assertNotNull(restaurant.getId());
    assertNotNull(restaurant.getProducts());
  }

  @Test
  public void orderItemRepresentation() {
    var quantity = 1;
    var orderId = new OrderId(UUID.randomUUID());
    var orderItemId = new OrderItemId(12L);
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(18.11));
    var product = new Product(productId, "Blue Cheese", price);
    //
    var subTotal = price.multiplyMoney(quantity);
    //
    var orderItem = new OrderItem(orderId, orderItemId, product, price, subTotal, quantity);
    //
    assertNotNull(orderItem.getId());
    assertNotNull(orderItem.getOrderId()); // Order
    assertEquals(orderItem.getOrderId().getValue(), orderId.getValue());
    assertEquals(orderItem.getSubTotal().getAmount(), price.multiplyMoney(quantity).getAmount());

  }

  @Test
  public void orderItemByTwoRepresentation() {
    var quantity = 2;
    var orderId = new OrderId(UUID.randomUUID());
    var orderItemId = new OrderItemId(12L);
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(3.32));
    var product = new Product(productId, "Butter", price);
    //
    var subTotal = price.multiplyMoney(quantity);
    //
    var orderItem = new OrderItem(orderId, orderItemId, product, price, subTotal, quantity);
    //
    assertNotNull(orderItem.getId());
    assertNotNull(orderItem.getOrderId());
    assertEquals(orderItem.getOrderId().getValue(), orderId.getValue());
    assertEquals(orderItem.getSubTotal().getAmount(), price.multiplyMoney(quantity).getAmount());

  }

  @Test
  public void validateInitializerOrderRepresentation() {
    var order = super.initializerOrderWithSingleOrderItemInitiateMock();
    order.initializerOrder();
    //
    assertNotNull(order.getId());
    assertNotNull(order.getTrackingId());
    assertNotNull(order.getItems().get(0).getId());
    assertEquals(OrderStatus.PENDING, order.getOrderStatus());
  }

  @Test
  public void validateOrderRepresentation() {
    var order = super.initializerToValidateOrderWithSingleOrderItemsInitiateMock();
    order.validateOrder();
    assertNull(order.getId());
    assertNotNull(order.getPrice());
  }

  @Test
  public void validateAndInitializerOrderRepresentation() {
    var order = super.initializerToValidateOrderWithSingleOrderItemsInitiateMock();
    order.validateOrder();
    order.initializerOrder();
    assertNotNull(order.getId());
    assertEquals(order.getOrderStatus(), OrderStatus.PENDING);
    assertNotNull(order.getPrice());
  }

  @Test
  public void validateOrderWithSingleOrderItemAndWrongSubTotalRepresentation() {
    var order = this.initializerOrderWithSingleOrderItemAndWrongSubTotalMock();
    var message = String.format(OrderDomainInfo.ORDER_ITEM_INVALID_PRICE, order.getPrice().getAmount(),
            order.getItems().get(0).getProduct().getId().getValue());
    //
    var exception = assertThrows(OrderDomainException.class, () -> {
      order.validateOrder();
      //
      log.info(message);
    }, message);
    //
    assertEquals(exception.getMessage(), message);

  }

  @Test
  public void validateOrderWithMultipleOrderItemRepresentation() {
    var order = this.initializerToValidateOrderWithMultipleOrderItemsInitiateMock();
    //
    order.validateOrder();
    assertNotNull(order.getPrice());
    assertNotNull(order.getItems());
    assertTrue(order.getPrice().isGreaterThan(order.getItems().get(0).getPrice()));
  }

  @Test
  public void validateOrderWithMultipleOrderItemAndWrongSubTotalRepresentation() {
    var order = this.initializerToValidateOrderWithMultipleOrderItemsAndWrongSubTotalMock();
    var message = String.format(OrderDomainInfo.ORDER_ITEM_INVALID_PRICE, order.getItems().get(1).getPrice().getAmount(),
            // second Product Item
            order.getItems().get(1).getProduct().getId().getValue());
    //
    var exception = assertThrows(OrderDomainException.class, () -> {
      order.validateOrder();
      //
      log.info(message);
    }, message);
    //
    assertEquals(exception.getMessage(), message);

  }

  @Test
  public void statePayTransitionRepresentation() {
    var order = initializerOrderWithSingleOrderItemInitiateMock();
    order.validateOrder();
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
    var order = super.initializerOrderWithSingleOrderItemInitiateMock();
    order.validateOrder();
    order.initializerOrder();
    assertNotNull(order.getId());
    assertNotNull(order.getTrackingId());
    //
    order.pay();
    assertEquals(OrderStatus.PAID, order.getOrderStatus());
    //
    var failedList = List.of("Not Approved of any Reason");
    order.initCancel(failedList);
    assertEquals(1, order.getFailureMessages().size());
    assertEquals(OrderStatus.CANCELLING, order.getOrderStatus());
  }


  @Test
  public void orderDomainServiceCreatedEventRepresentation() {
    var restaurant = super.restaurantInitiateMock();
    var order = super.initializerOrderWithSingleOrderItemInitiateMock();
    //
    var orderCreatedEvent = this.orderDomainService.validateAndInitiateOrder(order, restaurant);
    assertNotNull(orderCreatedEvent.getOrder().getId());
    assertEquals(orderCreatedEvent.getOrder().getOrderStatus(), OrderStatus.PENDING);
  }

  /*
  Fix the all testes bellow
  @Test
  @Disabled
  public void orderDomainServiceApproveOrderRepresentation() {
    var order = this.initializerOrderWithStatusMock(OrderStatus.PAID);
    var orderEventApproved = new OrderDomainServiceImpl();
    orderEventApproved.approveOrder(order);
    assertEquals(order.getOrderStatus(), OrderStatus.APPROVED);
  }

  @Test
  @Disabled
  public void orderDomainServiceCancelOrderRepresentation() {
    var order = this.initializerOrderWithStatusMock(OrderStatus.CANCELLING);
    var orderEventApproved = new OrderDomainServiceImpl();
    orderEventApproved.cancelOrder(order, List.of("Order Cancelled"));
    assertEquals(order.getOrderStatus(), OrderStatus.CANCELLED);
  }

  @Test
  @Disabled
  public void orderDomainServicePayOrderRepresentation() {
    var order = this.initializerOrderWithStatusMock(OrderStatus.PENDING);
    var orderEventApproved = new OrderDomainServiceImpl();
    orderEventApproved.payOrder(order);
    assertEquals(order.getOrderStatus(), OrderStatus.PAID);
  }
*/


}
