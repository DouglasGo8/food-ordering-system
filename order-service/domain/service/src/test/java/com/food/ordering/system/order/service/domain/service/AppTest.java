package com.food.ordering.system.order.service.domain.service;

import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderCommandDTO;
import com.food.ordering.system.order.service.domain.service.dto.create.CreateOrderResponseDTO;
import com.food.ordering.system.order.service.domain.service.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.service.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.service.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.service.dto.track.TrackOrderResponse;
import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import com.food.ordering.system.shared.domain.valueobject.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppTest implements BaseTest {

  @Test
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
    var paymentResponse = PaymentResponse
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
    assertEquals(PaymentStatus.COMPLETED, paymentResponse.getPaymentStatus());
  }

  @Test
  public void createRestaurantApprovalResponse() {
    var restaurantApprovalResponse = RestaurantApprovalResponse.builder()
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
  public void createTrackOrderQueryRepresentation() {
    var tracked = TrackOrderQuery.builder().orderTrackingId(UUID.randomUUID()).build();
    //
    assertNotNull(tracked.getOrderTrackingId());
  }

  @Test
  public void createTrackOrderResponseRepresentation() {
    var trackedResponse = TrackOrderResponse.builder()
            .orderTrackingId(UUID.randomUUID())
            .orderStatus(OrderStatus.APPROVED)
            .failureMessages(List.of(""))
            .build();
    //
    assertNotNull(trackedResponse.getOrderTrackingId());
  }



}
