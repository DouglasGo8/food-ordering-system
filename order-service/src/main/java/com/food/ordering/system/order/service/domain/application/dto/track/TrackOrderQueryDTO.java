package com.food.ordering.system.order.service.domain.application.dto.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderQueryDTO {
  @NotNull
  private final UUID orderTrackingId;
}
