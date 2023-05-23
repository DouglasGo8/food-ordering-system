package com.food.ordering.system.order.service.application.mediation.dto.exception;

public class RestaurantDataAccessException extends RuntimeException {
  public RestaurantDataAccessException(String message) {
    super(message);
  }
}
