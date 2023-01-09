package com.food.ordering.system.order.domain.application.service.ports.input.service.message.listener.restaurantapproval;

import com.food.ordering.system.order.domain.application.service.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalResponseMessageListener {
  void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);
  void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
