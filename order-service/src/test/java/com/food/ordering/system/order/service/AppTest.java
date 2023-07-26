package com.food.ordering.system.order.service;

import com.food.ordering.system.order.service.application.service.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.application.service.dto.create.CreateOrderResponseDTO;
import com.food.ordering.system.order.service.application.service.dto.create.OrderItemDTO;
import com.food.ordering.system.order.service.application.service.dto.message.PaymentResponseDTO;
import com.food.ordering.system.order.service.application.service.dto.message.RestaurantApprovalResponseDTO;
import com.food.ordering.system.order.service.application.service.dto.track.TrackOrderQueryDTO;
import com.food.ordering.system.order.service.application.service.dto.track.TrackOrderResponseDTO;
import com.food.ordering.system.order.service.application.service.mapper.OrderDataMapper;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Disabled
@QuarkusTest
public class AppTest implements BaseTest {

  @Inject
  OrderDataMapper mapper;

  @Test

  public void createOrderWithSingleItemCommandRepresentation() {
    var price = BigDecimal.valueOf(33_76L);
    var createOrderCommand = CreateOrderCommandDTO.builder()
            .customerId(UUID.randomUUID())
            .restaurantId(UUID.randomUUID())
            .price(BigDecimal.valueOf(33_76)) // Sum of Order Items Price
            .items(List.of(this.createSingleOrderItemMock(price)))
            .address(this.createOrderAddressMock())
            .build();

    assertNotNull(createOrderCommand.getCustomerId());
    assertNotNull(createOrderCommand.getRestaurantId());
    //
    log.info("AssertEquals Single Item");
    // Single Item
    assertEquals(createOrderCommand.getPrice(), createOrderCommand.getItems().get(0).getPrice());
  }

  @Test
  
  public void createOrderWithMultipleItemCommandRepresentation() {

    var items = this.createMultipleOrderItemsMock();
    var orderPrice = items.stream().map(OrderItemDTO::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

    var createOrderCommand = CreateOrderCommandDTO.builder()
            .customerId(UUID.randomUUID())
            .restaurantId(UUID.randomUUID())
            .price(orderPrice) // Sum of Order Items Price
            .items(items)
            .address(this.createOrderAddressMock())
            .build();

    assertNotNull(createOrderCommand.getCustomerId());
    assertNotNull(createOrderCommand.getRestaurantId());
    assertEquals(createOrderCommand.getPrice(), orderPrice);
  }

  @Test
  
  public void createOrderResponseRepresentation() {
    //
    var createOrderResponse = CreateOrderResponseDTO.builder()
            .message("Response from Space")
            .orderTrackingID(UUID.randomUUID())
            .orderStatus(OrderStatus.APPROVED)
            .build();
    //
    assertEquals(OrderStatus.APPROVED, createOrderResponse.getOrderStatus());
  }

  @Test
  
  public void createPaymentResponseRepresentation() {
    var paymentResponse = PaymentResponseDTO
            .builder()
            .id("01938e95-9e40-4d18-b550-c00ebfd41238")
            .sagaId("30593229-6fa0-4cab-a731-00503455cc3a")
            .orderId("fc13c77c-5aeb-4102-8678-8244064ea6e5")
            .paymentId("b3206282-bfc7-4d09-bf94-7ed1691672d5")
            .customerId("4df6e8f8-09a9-48b9-a0f7-6dc7fdb84985")
            .price(BigDecimal.valueOf(10_22L))
            .createdAt(Instant.now())
            .paymentStatus(PaymentStatus.COMPLETED)
            .failureMessages(List.of("")).build();
    //
    assertNotNull(paymentResponse.getId());
    assertEquals(PaymentStatus.COMPLETED, paymentResponse.getPaymentStatus());
  }

  @Test
  
  public void createRestaurantApprovalResponse() {
    var restaurantApprovalResponse = RestaurantApprovalResponseDTO.builder()
            .id("a9b7ba11-0aea-422a-b648-660b82342569")
            .sagaId("3d9df5dc-3263-45d8-89b2-bf545cf6205f")
            .orderId("57e0e4ce-9eab-44e5-98b1-575fa8763cd7")
            .restaurantId("50109882-bf00-46f6-a88b-133dbcf1f371")
            .createdAt(Instant.now())
            .failureMessages(List.of(""))
            .orderApprovalStatus(OrderApprovalStatus.APPROVED)
            .build();
    assertNotNull(restaurantApprovalResponse.getId());
    assertEquals(OrderApprovalStatus.APPROVED, restaurantApprovalResponse.getOrderApprovalStatus());
  }

  @Test
  
  public void createTrackOrderQueryRepresentation() {
    var tracked = TrackOrderQueryDTO.builder().orderTrackingId(UUID.randomUUID()).build();
    //
    assertNotNull(tracked.getOrderTrackingId());
  }

  @Test
  
  public void createTrackOrderResponseRepresentation() {
    var trackedResponse = TrackOrderResponseDTO.builder()
            .orderTrackingId(UUID.randomUUID())
            .orderStatus(OrderStatus.APPROVED)
            .failureMessages(List.of("A Failure Message"))
            .build();
    //
    assertNotNull(trackedResponse.getOrderTrackingId());
  }

  @Test
  
  public void orderDataMapperCreateOrderCommandToRestaurantRepresentation() {
    var body = this.createOrderCommandDTOFullMock();
    var restaurant = mapper.createOrderCommandToRestaurant(body);
    assertNotNull(restaurant.getId());
    assertFalse(restaurant.isActive());
  }

  // test error::Restaurant Object with new structure
  @Test
  @Disabled
  public void orderDataMapperCreateOrderCommandToOrderRepresentation() {
    var body = this.createOrderCommandDTOFullMock();
    var order = mapper.createOrderCommandToOrder(body);
    var restaurant = mapper.createOrderCommandToRestaurant(body);

    var orderCreatedEvent = mapper.validateAndInitializeOrder(order, restaurant);
    assertNotNull(orderCreatedEvent.getOrder().getId());
  }

  // test error::Restaurant Object with new structure
  @Test
  @Disabled
  public void orderDataMapperOrderToCreateOrderResponseDTO() {
    var body = this.createOrderCommandDTOFullMock();
    var order = mapper.createOrderCommandToOrder(body);
    var restaurant = mapper.createOrderCommandToRestaurant(body);
    var orderCreatedEvent = mapper.validateAndInitializeOrder(order, restaurant );
    var createOrderResponseDTO = mapper.orderToCreateOrderResponseDTO(orderCreatedEvent);
    assertNotNull(createOrderResponseDTO.getOrderTrackingID());
  }

  @Test
  
  public void orderDataMapperOrderToTrackOrderResponse() {
    var order = this.initializerToValidateOrderInitiateMock();
    order.validateOrder();
    order.initializerOrder();
    //
    var trackOrderResponseDTO = mapper.orderToTrackOrderResponse(order);
    assertEquals(OrderStatus.PENDING, trackOrderResponseDTO.getOrderStatus());
  }


}
