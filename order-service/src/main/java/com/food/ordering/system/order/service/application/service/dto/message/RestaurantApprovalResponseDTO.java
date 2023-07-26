package com.food.ordering.system.order.service.application.service.dto.message;

import com.food.ordering.system.shared.domain.valueobject.OrderApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalResponseDTO {
  private String id;
  private String sagaId;
  private String orderId;
  private String restaurantId;
  // ----------------------------------------
  private Instant createdAt;
  private List<String> failureMessages;
  private OrderApprovalStatus orderApprovalStatus;

}
