package com.food.ordering.system.order.service.domain.application.dto.create;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantProductsInfoDTO {
  private String restaurant_id;
  private String restaurant_name;
  private Boolean restaurant_active;
  private String product_id;
  private String product_name;
  private BigDecimal product_price;
  private Boolean product_available;
}
