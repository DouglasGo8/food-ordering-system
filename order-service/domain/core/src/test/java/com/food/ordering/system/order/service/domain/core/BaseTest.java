package com.food.ordering.system.order.service.domain.core;

import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public abstract class BaseTest {


  protected Restaurant restaurantInitiateMock() {
    var productId = new ProductId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(6.12));
    var product = new Product(productId, "Yoghurt", price);
    return new Restaurant(restaurantId, true, List.of(product));
  }

  protected Order initializerOrderWithSingleOrderItemInitiateMock() {
    //
    var quantity = 1;
    //var orderItemId = new OrderItemId(1L);
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(9.32));
    //var price = new Money(BigDecimal.ZERO);
    var customerId = new CustomerId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var product = new Product(productId, "Hamburger", price);
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    var subTotal = price.multiplyMoney(quantity);
    //
    var orderItem = new OrderItem(product, price, subTotal, quantity);
    //
    return new Order(subTotal, customerId, restaurantId, streetAddress, List.of(orderItem));
  }

  protected Order initializerOrderWithSingleOrderItemAndWrongSubTotalMock() {
    var quantity = 2;
    //
    var productId = new ProductId(UUID.randomUUID());
    var customerId = new CustomerId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(33.76));
    var product = new Product(productId, "Spare Ribs", price);
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    // Forces SubTotal equals price (throws Exception)
    var orderItem = new OrderItem(product, price, /* SubTotal Wrong*/ price, quantity);
    //
    return new Order(price, customerId, restaurantId, streetAddress, List.of(orderItem));
    //
  }

  // price's product doesn't match with orderItem price
  //initializerToValidateOrderWithSingleOrderItemsInitiateMock

  protected Order initializerToValidateOrderWithSingleOrderItemsInitiateMock() {
    var quantity = 1;
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(33.76));
    var subTotal = price.multiplyMoney(quantity);
    var product = new Product(productId, "Ham Gold", price);
    // Order Item
    var orderItem = new OrderItem(product, price, subTotal, quantity);
    // Common Info
    var customerId = new CustomerId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    return new Order(subTotal, customerId, restaurantId, streetAddress, List.of(orderItem));
  }

  protected Order initializerToValidateOrderWithMultipleOrderItemsInitiateMock() {
    var quantity_ONE = 1;
    var quantity_TWO = 2;
    var productId_ONE = new ProductId(UUID.randomUUID());
    var productId_TWO = new ProductId(UUID.randomUUID());
    //
    var price_ONE = new Money(BigDecimal.valueOf(99.02));
    var price_TWO = new Money(BigDecimal.valueOf(33.76));
    //
    var subTotal_ONE = price_ONE.multiplyMoney(quantity_ONE);
    var subTotal_TWO = price_TWO.multiplyMoney(quantity_TWO);
    //
    var product_ONE = new Product(productId_ONE, "Apricot himalayas", price_ONE);
    var product_TWO = new Product(productId_TWO, "Liver", price_TWO);
    // Order Item 1
    var orderItem1 = new OrderItem(product_ONE, price_ONE, subTotal_ONE, quantity_ONE);
    // Order Item 2
    var orderItem2 = new OrderItem(product_TWO, price_TWO, subTotal_TWO, quantity_TWO);
    // Common Info
    var customerId = new CustomerId(UUID.randomUUID());
    var orderPrice = subTotal_ONE.addMoney(subTotal_TWO);
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    return new Order(orderPrice, customerId, restaurantId, streetAddress, List.of(orderItem1, orderItem2));
  }

  protected Order initializerToValidateOrderWithMultipleOrderItemsAndWrongSubTotalMock() {
    var quantity_ONE = 1;
    var quantity_TWO = 2;
    var productId_ONE = new ProductId(UUID.randomUUID());
    var productId_TWO = new ProductId(UUID.randomUUID());
    //
    var price_ONE = new Money(BigDecimal.valueOf(99.02));
    var price_TWO = new Money(BigDecimal.valueOf(33.76));
    //
    var subTotal_ONE = price_ONE.multiplyMoney(quantity_ONE);
    var subTotal_TWO = price_TWO.multiplyMoney(quantity_ONE); // forces the Error
    //
    var product_ONE = new Product(productId_ONE, "Apricot himalayas", price_ONE);
    var product_TWO = new Product(productId_TWO, "Liver", price_TWO);
    // Order Item 1
    var orderItem1 = new OrderItem(product_ONE, price_ONE, subTotal_ONE, quantity_ONE);
    // Order Item 2
    var orderItem2 = new OrderItem(product_TWO, price_TWO, subTotal_TWO, quantity_TWO);
    // Common Info
    var customerId = new CustomerId(UUID.randomUUID());
    var orderPrice = subTotal_ONE.addMoney(subTotal_TWO);
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    return new Order(orderPrice, customerId, restaurantId, streetAddress, List.of(orderItem1, orderItem2));
  }
}
