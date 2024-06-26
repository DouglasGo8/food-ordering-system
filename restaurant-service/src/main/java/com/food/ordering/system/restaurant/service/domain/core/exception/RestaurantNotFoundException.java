package com.food.ordering.system.restaurant.service.domain.core.exception;

import com.food.ordering.system.shared.domain.exception.DomainException;

public class RestaurantNotFoundException extends DomainException {
  public RestaurantNotFoundException(String message) {
    super(message);
  }

  public RestaurantNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
