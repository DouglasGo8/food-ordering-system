package com.food.ordering.system.order.service;

import com.food.ordering.system.order.service.domain.application.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.domain.application.dto.create.OrderAddressDTO;
import com.food.ordering.system.order.service.domain.application.dto.create.OrderItemDTO;
import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.shared.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.shared.avro.model.PaymentStatus;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface BaseTest {

  default OrderItemDTO createSingleOrderItemMock(BigDecimal price) {

    return OrderItemDTO.builder()
            .productId(UUID.randomUUID())
            .quantity(1)
            .price(price)
            .subTotal(price)
            .build();
  }


  default List<OrderItemDTO> createMultipleOrderItemsMock() {
    var price_ONE = BigDecimal.valueOf(22.76);
    var price_TWO = BigDecimal.valueOf(77.14);
    var quantity_ONE = 1;
    var quantity_TWO = 2;
    //
    var orderItem_ONE = OrderItemDTO.builder()
            .productId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb47"))
            .quantity(quantity_ONE)
            .price(price_ONE)
            .subTotal(price_ONE.multiply(BigDecimal.valueOf(quantity_ONE)))
            .build();

    var orderItem_TWO = OrderItemDTO.builder()
            .productId(UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb48"))
            .quantity(quantity_TWO)
            .price(price_TWO)
            .subTotal(price_TWO.multiply(BigDecimal.valueOf(quantity_TWO)))
            .build();

    return List.of(orderItem_ONE, orderItem_TWO);
  }

  default List<OrderItemDTO> createMultipleOrderItemsWithWrongPriceMock() {
    var price_ONE = BigDecimal.valueOf(22.76);
    var price_TWO = BigDecimal.valueOf(77.14);
    var quantity_ONE = 1;
    var quantity_TWO = 2;
    //
    var orderItem_ONE = OrderItemDTO.builder()
            .productId(UUID.randomUUID())
            .quantity(quantity_ONE)
            .price(price_ONE)
            .subTotal(price_ONE.multiply(BigDecimal.valueOf(quantity_ONE)))
            .build();

    var orderItem_TWO = OrderItemDTO.builder()
            .productId(UUID.randomUUID())
            .quantity(quantity_TWO)
            .price(price_TWO)
            .subTotal(price_TWO.multiply(BigDecimal.valueOf(quantity_ONE)))
            .build();

    return List.of(orderItem_ONE, orderItem_TWO);
  }


  default OrderAddressDTO createOrderAddressMock() {
    return OrderAddressDTO.builder()
            .city("LA")
            .street("Avenue, 23 Lord Corner")
            .postalCode("21331")
            .build();
  }

  default CreateOrderCommandDTO createOrderCommandDTOFullMock() {

    var items = this.createMultipleOrderItemsMock();
    var customerIdValid = UUID.fromString("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9");
    var restaurantIdValid = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb45");
    //
    var orderPrice = items.stream().map(OrderItemDTO::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    //
    return CreateOrderCommandDTO.builder()
            .customerId(customerIdValid)
            .restaurantId(restaurantIdValid)
            .price(orderPrice)
            .items(items)
            .address(this.createOrderAddressMock())
            .build();

  }

  default CreateOrderCommandDTO createOrderCommandDTOFullMockWithInvalidUUID() {

    var items = this.createMultipleOrderItemsMock();
    var customerIdValid = UUID.fromString("40a52a1d-bdd9-46ee-b002-14458de52721");
    var restaurantIdValid = UUID.fromString("66c869d2-79a9-402f-8617-f7796f648725");
    //
    var orderPrice = items.stream().map(OrderItemDTO::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    //
    return CreateOrderCommandDTO.builder()
            .customerId(customerIdValid)
            .restaurantId(restaurantIdValid)
            .price(orderPrice)
            .items(items)
            .address(this.createOrderAddressMock())
            .build();

  }

  default Order initializerToValidateOrderInitiateMock() {
    //
    var quantity = 1;
    var orderItemId = new OrderItemId(1L);
    var productId = new ProductId(UUID.randomUUID());
    var price = new Money(BigDecimal.valueOf(9.32));

    //var price = new Money(BigDecimal.ZERO);
    var customerId = new CustomerId(UUID.randomUUID());
    var restaurantId = new RestaurantId(UUID.randomUUID());
    var product = new Product(productId, "Chips", price);
    var streetAddress = new StreetAddress(UUID.randomUUID(), "NY", "Avenue 5th 566", "122002");
    //
    var subTotal = price.multiplyMoney(quantity);
    //var subTotal = price.multiplyMoney(2);
    //
    //var orderItem = List.of(new OrderItem(orderItemId, product, price, subTotal, quantity));
    var orderItem = List.of(OrderItem.builder()
                    .orderItemId(orderItemId)
                    .product(product)
                    .price(price)
                    .subTotal(subTotal)
                    .quantity(quantity)
            .build());
    //
    return Order.builder()
            .price(price)
            .customerId(customerId)
            .restaurantId(restaurantId)
            .deliveryAddress(streetAddress)
            .items(orderItem)
            .build();
            //new Order(price, customerId, restaurantId, streetAddress, List.of(orderItem));
  }

  default PaymentResponseAvroModel createPaymentResponseCompletedMock() {
    //
    return PaymentResponseAvroModel.newBuilder()
            .setId("01938e95-9e40-4d18-b550-c00ebfd41238")
            .setSagaId(UUID.randomUUID().toString())
            .setOrderId("ec78b161-3899-4866-8753-886b84a8fbce")
            .setPaymentId("cc326c0d-3e8b-4d48-8ab8-d1cb7c8dc9ae")
            .setCustomerId("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9")
            .setPrice(BigDecimal.valueOf(10_22L))
            .setCreatedAt(Instant.now())
            .setPaymentStatus(PaymentStatus.COMPLETED)
            .setFailureMessages(List.of(""))
            .build();
  }

  default PaymentResponseAvroModel createPaymentResponseCancelledMock() {
    //
    return PaymentResponseAvroModel.newBuilder()
            .setId("01938e95-9e40-4d18-b550-c00ebfd41238")
            .setSagaId(UUID.randomUUID().toString())
            .setOrderId("ec78b161-3899-4866-8753-886b84a8fbce")
            .setPaymentId("097cb4f0-49c1-4162-b480-1af478d40f22")
            .setCustomerId("af20558e-5e77-4a6e-bb2f-fef1f14c0ee9")
            .setPrice(BigDecimal.valueOf(10_22L))
            .setCreatedAt(Instant.now())
            .setPaymentStatus(PaymentStatus.CANCELLED)
            .setFailureMessages(List.of(""))
            .build();
  }
}