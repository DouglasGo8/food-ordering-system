package com.food.ordering.system.order.service.domain.application.dto.create;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommandDTO {
  @NotNull
  private UUID customerId;
  @NotNull
  private UUID restaurantId;
  @NotNull
  private BigDecimal price;
  @NotNull
  private List<OrderItemDTO> items;
  @NotNull
  private OrderAddressDTO address;
}
