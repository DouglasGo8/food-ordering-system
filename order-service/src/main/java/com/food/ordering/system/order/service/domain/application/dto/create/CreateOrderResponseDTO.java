package com.food.ordering.system.order.service.domain.application.dto.create;

import com.food.ordering.system.shared.domain.valueobject.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponseDTO {

  @NotNull
  private String message;
  @NotNull
  private UUID orderTrackingID;
  @NotNull
  private OrderStatus orderStatus;

}
