package com.food.ordering.system.order.service.application;

import com.food.ordering.system.order.service.application.mediation.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.application.mediation.dto.create.OrderAddressDTO;
import com.food.ordering.system.order.service.application.mediation.dto.create.OrderItemDTO;
import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BaseTest {

  default OrderItemDTO createOrderItemMock(BigDecimal price) {
    //var price = BigDecimal.valueOf(200_21L);
    return OrderItemDTO.builder()
            .productId(UUID.randomUUID())
            .quantity(1)
            .price(price)
            .subTotal(price)
            .build();
  }

  default OrderAddressDTO createOrderAddressMock() {
    return OrderAddressDTO.builder()
            .city("LA")
            .street("Avenue, 23 Lord Corner")
            .postalCode("21331")
            .build();
  }

  default CreateOrderCommandDTO createOrderCommandDTOFullMock() {
    var price = BigDecimal.valueOf(33.76);
    var customerIdValid = UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9");
    var restaurantIdValid = UUID.fromString("c8dfc68d-9269-45c2-b2d1-7e0d0aa3c57b");


    return CreateOrderCommandDTO.builder()
            .customerId(customerIdValid)
            .restaurantId(restaurantIdValid)
            .price(price)
            .items(List.of(this.createOrderItemMock(price)))
            .address(this.createOrderAddressMock())
            .build();

  }

  default Order initializerToValidateOrderInitiateMock() {
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
