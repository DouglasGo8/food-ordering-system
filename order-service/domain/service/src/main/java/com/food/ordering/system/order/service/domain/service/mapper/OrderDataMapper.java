package com.food.ordering.system.order.service.domain.service.mapper;


import com.food.ordering.system.order.service.domain.core.OrderDomainService;
import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderResponseDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.OrderAddressDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.OrderItemDTO;
import com.food.ordering.system.order.service.domain.service.dto.track.TrackOrderResponseDTO;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import io.quarkus.runtime.Startup;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Named
@Startup
@NoArgsConstructor
@ApplicationScoped
public class OrderDataMapper {

  @Inject
  OrderDomainService orderDomainService;

  public Restaurant createOrderCommandToRestaurant(CreateOrderCommandDTO createOrderCommand) {
    var restaurantId = new RestaurantId(createOrderCommand.getRestaurantId());
    //
    final Function<List<OrderItemDTO>, List<Product>> transform = (items) ->
            items.stream().map(item -> new Product(new ProductId(item.getProductId()))).collect(Collectors.toList());
    //
    return new Restaurant(restaurantId, true, transform.apply(createOrderCommand.getItems()));
    //
  }

  @PostConstruct
  public void setUp() {
    //this.orderDomainService = new OrderDomainServiceImpl();
  }

  public OrderCreatedEvent validateAndInitializeOrder(Order order, Restaurant restaurant) {
    //var orderDomainService = new OrderDomainServiceImpl();
    return orderDomainService.validateAndInitiateOrder(order, restaurant);
    //return null;
  }

  public Order createOrderCommandToOrder(CreateOrderCommandDTO createOrderCommand) {
    var money = new Money(createOrderCommand.getPrice());
    var items = this.orderItemsToOrderItemEntities(createOrderCommand.getItems());
    var customerId = new CustomerId(createOrderCommand.getCustomerId());
    var restaurantId = new RestaurantId(createOrderCommand.getRestaurantId());
    var deliveryAddress = this.orderAddressToStreetAddress(createOrderCommand.getAddress());
    return new Order(money, customerId, restaurantId, deliveryAddress, items);
    //order.setId(new OrderId(UUID.randomUUID()));
    //return order;
  }

  public CreateOrderResponseDTO orderToCreateOrderResponseDTO(OrderCreatedEvent orderCreatedEvent, String message
  ) {
    final var order = orderCreatedEvent.getOrder();
    return CreateOrderResponseDTO.builder()
            .orderTrackingID(order.getTrackingId().getValue())
            .orderStatus(order.getOrderStatus())
            .message(message)
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

    return items.stream().map(i -> new OrderItem
                    (  // Product
                            new Product(new ProductId(i.getProductId())),
                            new Money(i.getPrice()),
                            new Money(i.getPrice()), 1))
            .toList();
  }
}
