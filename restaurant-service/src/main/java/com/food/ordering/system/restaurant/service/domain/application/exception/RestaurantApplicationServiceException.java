package com.food.ordering.system.restaurant.service.domain.application.exception;

import com.food.ordering.system.shared.domain.exception.DomainException;

public class RestaurantApplicationServiceException extends DomainException {
  public RestaurantApplicationServiceException(String message) {
    super(message);
  }
  public RestaurantApplicationServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
