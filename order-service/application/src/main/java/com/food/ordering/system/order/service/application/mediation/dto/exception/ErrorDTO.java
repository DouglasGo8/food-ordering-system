package com.food.ordering.system.order.service.application.mediation.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {
  private final String code;
  private final String message;
}
