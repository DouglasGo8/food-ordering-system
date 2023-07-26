package com.food.ordering.system.shared.order.core.service.domain.exception;

import com.food.ordering.system.shared.domain.exception.DomainException;

public class OrderDomainNotFound extends DomainException {
  public OrderDomainNotFound(String message) {
    super(message);
  }

  public OrderDomainNotFound(String message, Throwable cause) {
    super(message, cause);
  }
}
