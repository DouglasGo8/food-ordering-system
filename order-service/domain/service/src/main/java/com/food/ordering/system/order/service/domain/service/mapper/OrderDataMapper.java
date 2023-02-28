package com.food.ordering.system.order.service.domain.service.mapper;


import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderResponseDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.OrderAddressDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.OrderItemDTO;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import lombok.NoArgsConstructor;
import org.apache.camel.Body;
import org.apache.camel.Header;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Named
@NoArgsConstructor
@ApplicationScoped
public class OrderDataMapper {

  public Restaurant createOrderCommandToRestaurant(@Body CreateOrderCommandDTO createOrderCommand) {
    var restaurantId = new RestaurantId(createOrderCommand.getRestaurantId());
    //
    final Function<List<OrderItemDTO>, List<Product>> transform = (items) ->
            items.stream().map(item -> new Product(new ProductId(item.getProductId()))).collect(Collectors.toList());
    //
    return new Restaurant(restaurantId, true, transform.apply(createOrderCommand.getItems()));
    //
  }

  public Order createOrderCommandToOrder(@Body CreateOrderCommandDTO createOrderCommand) {
    var money = new Money(createOrderCommand.getPrice());
    var items = this.orderItemsToOrderItemEntities(createOrderCommand.getItems());
    var customerId = new CustomerId(createOrderCommand.getCustomerId());
    var restaurantId = new RestaurantId(createOrderCommand.getRestaurantId());
    var deliveryAddress = this.orderAddressToStreetAddress(createOrderCommand.getAddress());
    return new Order(money, customerId, restaurantId, deliveryAddress, items);
  }

  public CreateOrderResponseDTO orderToCreateOrderResponseDTO(@Body Order order) {
    return CreateOrderResponseDTO.builder()
            .orderTrackingID(order.getTrackingId().getValue())
            .orderStatus(order.getOrderStatus())
            .build();
  }

  private StreetAddress orderAddressToStreetAddress(OrderAddressDTO orderAddress) {
    return new StreetAddress(UUID.randomUUID(), orderAddress.getCity(),
            orderAddress.getStreet(), orderAddress.getPostalCode());
  }

  private List<OrderItem> orderItemsToOrderItemEntities(List<OrderItemDTO> items) {
    return items.stream().map(item -> new OrderItem(new Product(new ProductId(item.getProductId())),
            new Money(item.getPrice()), new Money(item.getPrice()), item.getQuantity())).collect(Collectors.toList());
  }
}
