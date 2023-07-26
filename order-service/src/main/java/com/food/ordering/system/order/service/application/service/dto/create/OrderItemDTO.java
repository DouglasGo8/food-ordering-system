package com.food.ordering.system.order.service.application.service.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

  @NotNull
  private UUID productId;
  @NotNull
  private Integer quantity;
  @NotNull
  private BigDecimal price;
  @NotNull
  private BigDecimal subTotal;
}
