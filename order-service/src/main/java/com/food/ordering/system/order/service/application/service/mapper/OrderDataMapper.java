package com.food.ordering.system.order.service.application.service.mapper;

import com.food.ordering.system.order.service.application.service.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.application.service.dto.create.CreateOrderResponseDTO;
import com.food.ordering.system.order.service.application.service.dto.create.OrderAddressDTO;
import com.food.ordering.system.order.service.application.service.dto.create.OrderItemDTO;
import com.food.ordering.system.order.service.application.service.dto.track.TrackOrderResponseDTO;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import com.food.ordering.system.shared.order.core.service.domain.OrderDomainService;
import com.food.ordering.system.shared.order.core.service.domain.entity.Order;
import com.food.ordering.system.shared.order.core.service.domain.entity.OrderItem;
import com.food.ordering.system.shared.order.core.service.domain.entity.Product;
import com.food.ordering.system.shared.order.core.service.domain.entity.Restaurant;
import com.food.ordering.system.shared.order.core.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.shared.order.core.service.domain.valueobject.StreetAddress;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class OrderDataMapper {

  @Inject
  OrderDomainService orderDomainService;

  public Restaurant createOrderCommandToRestaurant(@Valid @Body CreateOrderCommandDTO createOrderCommand) {
    var restaurantId = new RestaurantId(createOrderCommand.getRestaurantId());
    //
    final Function<List<OrderItemDTO>, List<Product>> transform = (items) -> items.stream().map(item ->
            new Product(new ProductId(item.getProductId()))).collect(Collectors.toList());
    //
    return new Restaurant(restaurantId, transform.apply(createOrderCommand.getItems()));
    //
  }

  public OrderCreatedEvent validateAndInitializeOrder(@Body Order order, @ExchangeProperty("restaurantInfo") Restaurant restaurant) {
    return orderDomainService.validateAndInitiateOrder(order, restaurant);
  }

  public Order createOrderCommandToOrder(@ExchangeProperty("payload") @Valid CreateOrderCommandDTO createOrderCommand) {
    var orderPrice = new Money(createOrderCommand.getPrice());
    var items = this.orderItemsToOrderItemEntities(createOrderCommand.getItems());
    var customerId = new CustomerId(createOrderCommand.getCustomerId());
    var restaurantId = new RestaurantId(createOrderCommand.getRestaurantId());
    var deliveryAddress = this.orderAddressToStreetAddress(createOrderCommand.getAddress());
    return new Order(orderPrice, customerId, restaurantId, deliveryAddress, items);
  }

  public CreateOrderResponseDTO orderToCreateOrderResponseDTO(@Body OrderCreatedEvent orderCreatedEvent) {
    final var order = orderCreatedEvent.getOrder();
    return CreateOrderResponseDTO.builder()
            .orderTrackingID(order.getTrackingId().getValue())
            .orderStatus(order.getOrderStatus())
            .message(DomainConstants.CREATE_ORDER_SUCCESS)
            .build();
  }

  public TrackOrderResponseDTO orderToTrackOrderResponse(Order order) {
    return TrackOrderResponseDTO.builder()
            .orderTrackingId(order.getTrackingId().getValue())
            .orderStatus(order.getOrderStatus())
            .failureMessages(order.getFailureMessages())
            .build();
  }

  private StreetAddress orderAddressToStreetAddress(OrderAddressDTO orderAddress) {
    return new StreetAddress(UUID.randomUUID(), orderAddress.getCity(),
            orderAddress.getStreet(), orderAddress.getPostalCode());
  }

  private List<OrderItem> orderItemsToOrderItemEntities(List<OrderItemDTO> items) {
    return items.stream().map(item -> new OrderItem(
            new Product(new ProductId(item.getProductId()), new Money(item.getPrice())),
            new Money(item.getPrice()),
            new Money(item.getSubTotal()),
            item.getQuantity())).toList();
  }

}
