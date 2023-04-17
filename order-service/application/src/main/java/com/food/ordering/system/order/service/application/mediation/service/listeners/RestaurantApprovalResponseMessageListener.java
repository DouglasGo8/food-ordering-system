package com.food.ordering.system.order.service.application.mediation.service.listeners;

import com.food.ordering.system.order.service.application.mediation.dto.message.RestaurantApprovalResponseDTO;

public interface RestaurantApprovalResponseMessageListener {
  void orderApproved(RestaurantApprovalResponseDTO restaurantApprovalResponse);

  void orderRejected(RestaurantApprovalResponseDTO restaurantApprovalResponse);
}
