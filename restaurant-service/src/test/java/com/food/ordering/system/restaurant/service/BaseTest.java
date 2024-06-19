package com.food.ordering.system.restaurant.service;

import com.food.ordering.system.restaurant.service.domain.application.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.core.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.domain.core.entity.OrderDetail;
import com.food.ordering.system.restaurant.service.domain.core.entity.Product;
import com.food.ordering.system.restaurant.service.domain.core.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.core.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.core.event.OrderRejectedEvent;
import com.food.ordering.system.restaurant.service.domain.core.valueobject.OrderApprovalId;
import com.food.ordering.system.shared.domain.valueobject.*;


import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public interface BaseTest {

  default Product productMock() {
    var money = new Money(BigDecimal.valueOf(new Random().nextDouble(10, 100)));
    //
    return Product.Builder.builder()
            .productId(new ProductId(UUID.randomUUID()))
            .available(true)
            .name("Product 1")
            .price(money)
            .quantity(1)
            .build();
  }

  default List<Product> productMockList() {

    // --------------------------------------------- ITEM 1
    return List.of(Product.Builder.builder()
                    .productId(new ProductId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb47")))
                    .available(true)
                    .name("Product 1")
                    .price(new Money(BigDecimal.valueOf(22.76)))
                    .quantity(1)
                    .build(),
            // --------------------------------------------- ITEM 2
            Product.Builder.builder()
                    .productId(new ProductId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb48")))
                    .available(true)
                    .name("Product 2")
                    .price(new Money(BigDecimal.valueOf(77.14)))
                    .quantity(2)
                    .build()
    );
  }

  default OrderApproval orderApprovaldMock() {
    return OrderApproval.Builder.builder()
            .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
            .orderApprovalStatus(OrderApprovalStatus.APPROVED)
            .orderId(new OrderId(UUID.randomUUID()))
            .restaurantId(new RestaurantId(UUID.randomUUID()))
            .build();
  }

  default OrderDetail orderDetailMock() {
    return OrderDetail.Builder.builder()
            .orderStatus(OrderStatus.PAID)
            .orderId(new OrderId(UUID.randomUUID()))
            .totalAmount(new Money(BigDecimal.valueOf(22.20)))
            .products(List.of(this.productMock()))
            .build();
  }

  default OrderDetail orderDetailWithListOfProducts() {
    var productList = this.productMockList();
    var totalAmount = productList.stream()
            .map(p -> p.getPrice().multiplyMoney(p.getQuantity()))
            .reduce(Money.ZERO, Money::addMoney);

    return OrderDetail.Builder.builder()
            .orderStatus(OrderStatus.PAID)
            .orderId(new OrderId(UUID.randomUUID()))
            .totalAmount(totalAmount)
            .products(productList)
            .build();
  }

  default Restaurant restaurantMock() {

    var orderApproval = this.orderApprovaldMock();

    return Restaurant.Builder.builder()
            .restaurantId(orderApproval.getRestaurantId())
            .orderApproval(this.orderApprovaldMock())
            .active(true)
            .orderDetail(this.orderDetailWithListOfProducts())
            .build();
  }

  default OrderApprovedEvent orderApprovedEventMock() {
    return new OrderApprovedEvent(this.orderApprovaldMock(),
            new RestaurantId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb45")),
            List.of(), ZonedDateTime.now());
  }

  default OrderRejectedEvent orderRejectedEventMock() {
    return new OrderRejectedEvent(this.orderApprovaldMock(),
            new RestaurantId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb45")),
            List.of(), ZonedDateTime.now());
  }

  default RestaurantApprovalRequest restaurantApprovalRequestMock() {
    return RestaurantApprovalRequest.builder()
            .id(UUID.randomUUID().toString())
            .restaurantId("d215b5f8-0249-4dc5-89a3-51fd148cfb45")
            .orderId("ec78b161-3899-4866-8753-886b84a8fbce")
            .sagaId("")
            .restaurantOrderStatus(RestaurantOrderStatus.PAID)
            .price(BigDecimal.valueOf(177.04))
            .createdAt(Instant.now())
            .products(this.productMockList())
            .build();
  }
}
