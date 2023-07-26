package com.food.ordering.system.order.service.application.service.dto.create;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RestaurantProductsInfoDTO {
  private String restaurant_id;
  private String restaurant_name;
  private Boolean restaurant_active;
  private String product_id;
  private String product_name;
  private BigDecimal product_price;
  private Boolean product_available;
}
