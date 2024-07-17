package com.food.ordering.system.order.service.domain.application.dto.create;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemsAddressViewDTO {
  private String order_id;
  private String customer_id;
  private String restaurant_id;
  private String tracking_id;
  private double order_total_price;
  private String order_status;
  private String failure_messages;
  private String order_item_id;
  private String product_id;
  private double order_item_price;
  private int quantity;
  private double sub_total;
  private String order_address_id;
  private String city;
  private String street;
  private String postal_code;
}
