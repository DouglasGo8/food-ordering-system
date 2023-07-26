package com.food.ordering.system.order.service.application.service.dto.create;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddressDTO {
  @NotNull
  @Max(value = 50)
  private String street;

  @NotNull
  @Max(value = 10)
  private String postalCode;

  @NotNull
  @Max(value = 50)
  private String city;

}
