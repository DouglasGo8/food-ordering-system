package com.food.ordering.system.order.service.domain.application.mapper;

import com.food.ordering.system.order.service.domain.application.OrderDomainService;
import com.food.ordering.system.order.service.domain.application.OrderDomainServiceImpl;
import com.food.ordering.system.order.service.domain.application.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.domain.application.dto.create.CreateOrderResponseDTO;
import com.food.ordering.system.order.service.domain.application.dto.create.OrderAddressDTO;
import com.food.ordering.system.order.service.domain.application.dto.create.OrderItemDTO;
import com.food.ordering.system.order.service.domain.application.dto.track.TrackOrderResponseDTO;
import com.food.ordering.system.order.service.domain.core.entity.Order;
import com.food.ordering.system.order.service.domain.core.entity.OrderItem;
import com.food.ordering.system.order.service.domain.core.entity.Product;
import com.food.ordering.system.order.service.domain.core.entity.Restaurant;
import com.food.ordering.system.order.service.domain.core.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.core.valueobject.StreetAddress;
import com.food.ordering.system.shared.domain.DomainConstants;
import com.food.ordering.system.shared.domain.valueobject.CustomerId;
import com.food.ordering.system.shared.domain.valueobject.Money;
import com.food.ordering.system.shared.domain.valueobject.ProductId;
import com.food.ordering.system.shared.domain.valueobject.RestaurantId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperty;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class OrderDataMapper {

  //@Inject
  OrderDomainService orderDomainService;

  public OrderDataMapper() {
    this.orderDomainService = new OrderDomainServiceImpl();
  }

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
    return this.orderDomainService.validateAndInitiateOrder(order, restaurant);
  }

  public Order createOrderCommandToOrder(@ExchangeProperty("payload") @Valid CreateOrderCommandDTO createOrderCommand) {
    var orderPrice = new Money(createOrderCommand.getPrice());
    var items = this.orderItemsToOrderItemEntities(createOrderCommand.getItems());
    var customerId = new CustomerId(createOrderCommand.getCustomerId());
    var restaurantId = new RestaurantId(createOrderCommand.getRestaurantId());
    var deliveryAddress = this.orderAddressToStreetAddress(createOrderCommand.getAddress());
    //
    return Order.builder().price(orderPrice).customerId(customerId).restaurantId(restaurantId)
            .deliveryAddress(deliveryAddress)
            .items(items)
            .build();

    //Order(orderPrice, customerId, restaurantId, deliveryAddress, items);
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
    return items.stream().map(item ->
                    OrderItem.builder()
                            .product(new Product(new ProductId(item.getProductId())))
                            .price(new Money(item.getPrice()))
                            .subTotal(new Money(item.getSubTotal()))
                            .quantity(item.getQuantity())
                            .build())
            .collect(Collectors.toList());


  }

}
