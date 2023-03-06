package com.food.ordering.system.order.service.domain.service;

import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.OrderAddressDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.OrderItemDTO;

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
}
