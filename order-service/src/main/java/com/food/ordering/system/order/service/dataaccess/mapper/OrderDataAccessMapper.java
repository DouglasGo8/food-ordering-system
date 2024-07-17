package com.food.ordering.system.order.service.dataaccess.mapper;

import com.food.ordering.system.order.service.domain.application.dto.create.OrderItemsAddressViewDTO;
import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.core.valueobject.TrackingId;
import com.food.ordering.system.shared.domain.valueobject.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class OrderDataAccessMapper {

  @Handler
  public Order orderResultSetToOrder(@Body OrderItemsAddressViewDTO[] orders) {

    //    var items = (ArrayList<OrderItemsViewDTO>)rows.get(1);
    //  var orderEntity = (OrderItemsAddressViewDTO)rows.get(0);

    var orderEntity = Arrays.stream(orders).toList().getFirst();
    var orderItemsEntity = Arrays.stream(orders)
            .map(e -> OrderItem.builder()
                    .orderItemId(new OrderItemId(Long.parseLong(e.getOrder_item_id())))
                    .product(new Product(new ProductId(UUID.fromString(e.getProduct_id()))))
                    .price(new Money(BigDecimal.valueOf(e.getOrder_item_price())))
                    .quantity(e.getQuantity())
                    .subTotal(new Money(BigDecimal.valueOf(e.getSub_total())))
                    .build())
            .toList();

    //log.info("{}", rows);

    //log.info("===> {}",rows);

    //log.info(orderAddressView.getOrder_status());

    return Order.builder()
            .orderId(new OrderId(UUID.fromString(orderEntity.getOrder_id())))
            .customerId(new CustomerId(UUID.fromString(orderEntity.getCustomer_id())))
            .restaurantId(new RestaurantId(UUID.fromString(orderEntity.getRestaurant_id())))
            .deliveryAddress(new StreetAddress(UUID.fromString(orderEntity.getOrder_address_id()),
                    orderEntity.getStreet(),
                    orderEntity.getPostal_code(),
                    orderEntity.getCity()))
            .price(new Money(BigDecimal.valueOf(orderEntity.getOrder_total_price())))
            .items(orderItemsEntity)
            .trackingId(new TrackingId(UUID.fromString(orderEntity.getTracking_id())))
            .orderStatus(OrderStatus.valueOf(orderEntity.getOrder_status()))
            .failureMessages(orderEntity.getFailure_messages().isEmpty() ? List.of("") :
                    new ArrayList<>(Arrays.asList(orderEntity.getFailure_messages()
                            .split(","))))
            .build();
  }

}
