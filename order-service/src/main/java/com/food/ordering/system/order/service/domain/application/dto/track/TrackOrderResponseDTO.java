package com.food.ordering.system.order.service.domain.application.dto.track;

import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderResponseDTO {
  @NotNull
  private final UUID orderTrackingId;
  @NotNull

  private final OrderStatus orderStatus;
  private final List<String> failureMessages;
}
