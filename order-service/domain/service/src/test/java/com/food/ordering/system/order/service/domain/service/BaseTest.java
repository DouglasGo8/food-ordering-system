package com.food.ordering.system.order.service.domain.service;

import com.food.ordering.system.order.service.domain.service.dto.create.OrderAddressDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.OrderItemDTO;

import java.math.BigDecimal;
import java.util.UUID;

public interface BaseTest {

  default OrderItemDTO createOrderItemMock(BigDecimal price) {
    //var price = BigDecimal.valueOf(200_21L);
    return OrderItemDTO.builder()
            .productId(UUID.randomUUID())
            .quantity(1)
            .price(price)
            .subTotal(price).build();
  }

  default OrderAddressDTO createOrderAddressMock() {
    return OrderAddressDTO.builder()
            .city("LA")
            .street("Avenue, 23 Lord Corner")
            .postalCode("21331")
            .build();
  }
}
