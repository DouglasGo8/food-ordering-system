package com.food.ordering.system.order.service.domain.service.dto.create;

import com.food.ordering.system.shared.domain.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

  @NotNull
  private final String message;
  @NotNull
  private final UUID orderTrackingID;
  @NotNull
  private final OrderStatus orderStatus;

}
