package com.food.ordering.system.order.service.application;

import com.food.ordering.system.order.service.application.mediation.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.application.mediation.dto.create.CreateOrderResponseDTO;
import com.food.ordering.system.order.service.application.mediation.dto.message.PaymentResponseDTO;
import com.food.ordering.system.order.service.application.mediation.dto.message.RestaurantApprovalResponseDTO;
import com.food.ordering.system.order.service.application.mediation.dto.track.TrackOrderQueryDTO;
import com.food.ordering.system.order.service.application.mediation.dto.track.TrackOrderResponseDTO;
import com.food.ordering.system.order.service.application.mediation.mapper.OrderDataMapper;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AppTest implements BaseTest {

  @Inject
  OrderDataMapper mapper;

  @Test
  @Disabled
  public void createOrderCommandRepresentation() {
    var price = BigDecimal.valueOf(33_76L);
    var createOrderCommand = CreateOrderCommandDTO.builder()
            .customerId(UUID.randomUUID())
            .restaurantId(UUID.randomUUID())
            .price(BigDecimal.valueOf(33_76))
            .items(List.of(this.createOrderItemMock(price)))
            .address(this.createOrderAddressMock())
            .build();

    assertNotNull(createOrderCommand.getCustomerId());
  }

  @Test
  @Disabled
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
  @Disabled
  public void createPaymentResponseRepresentation() {
    var paymentResponse = PaymentResponseDTO
            .builder()
            .id("id-xxx")
            .sagaId("saga-id-xxx")
            .orderId("order-id-xxx")
            .paymentId("payment-id-xxx")
            .customerId("customer-id-xxx")
            .price(BigDecimal.valueOf(10_22L))
            .createdAt(Instant.now())
            .paymentStatus(PaymentStatus.COMPLETED)
            .failureMessages(List.of("")).build();
    //
    assertEquals(PaymentStatus.COMPLETED, paymentResponse.getPaymentStatus());
  }

  @Test
  @Disabled
  public void createRestaurantApprovalResponse() {
    var restaurantApprovalResponse = RestaurantApprovalResponseDTO.builder()
            .id("id-xxx")
            .sagaId("saga-id-xxx")
            .orderId("order-id-xxx")
            .restaurantId("restaurant-id-xxx")
            .createdAt(Instant.now())
            .failureMessages(List.of(""))
            .orderApprovalStatus(OrderApprovalStatus.APPROVED)
            .build();
    assertEquals("id-xxx", restaurantApprovalResponse.getId());
    assertEquals(OrderApprovalStatus.APPROVED, restaurantApprovalResponse.getOrderApprovalStatus());
  }

  @Test
  @Disabled
  public void createTrackOrderQueryRepresentation() {
    var tracked = TrackOrderQueryDTO.builder().orderTrackingId(UUID.randomUUID()).build();
    //
    assertNotNull(tracked.getOrderTrackingId());
  }

  @Test
  @Disabled
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
  @Disabled
  public void orderDataMapperCreateOrderCommandToRestaurantRepresentation() {
    var body = this.createOrderCommandDTOFullMock();
    var restaurant = mapper.createOrderCommandToRestaurant(body);
    assertNotNull(restaurant.getId());
    assertTrue(restaurant.isActive());
  }

  @Test
  @Disabled
  public void orderDataMapperCreateOrderCommandToOrderRepresentation() {
    var body = this.createOrderCommandDTOFullMock();
    var order = mapper.createOrderCommandToOrder(body);
    var restaurant = mapper.createOrderCommandToRestaurant(body);
    var orderCreatedEvent = mapper.validateAndInitializeOrder(order, restaurant);
    assertNotNull(orderCreatedEvent.getOrder().getId());
  }

  @Test
  @Disabled
  public void orderDataMapperOrderToCreateOrderResponseDTO() {
    var message = "My Message";
    var body = this.createOrderCommandDTOFullMock();
    var order = mapper.createOrderCommandToOrder(body);
    var restaurant = mapper.createOrderCommandToRestaurant(body);
    var orderCreatedEvent = mapper.validateAndInitializeOrder(order, restaurant);
    var createOrderResponseDTO = mapper.orderToCreateOrderResponseDTO(orderCreatedEvent, message);
    assertNotNull(createOrderResponseDTO.getOrderTrackingID());
  }

  @Test
  @Disabled
  public void orderDataMapperOrderToTrackOrderResponse() {
    var order = this.initializerToValidateOrderInitiateMock();
    //
    order.initializerOrder();
    //
    var trackOrderResponseDTO = mapper.orderToTrackOrderResponse(order);
    assertEquals(OrderStatus.PENDING, trackOrderResponseDTO.getOrderStatus());
  }


}
