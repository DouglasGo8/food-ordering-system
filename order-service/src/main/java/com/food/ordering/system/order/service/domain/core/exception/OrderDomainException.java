package com.food.ordering.system.order.service.domain.core.exception;

import com.food.ordering.system.shared.domain.exception.DomainException;

public class OrderDomainException extends DomainException {
  public OrderDomainException(String message) {
    super(message);
  }

  public OrderDomainException(String message, Throwable e) {
    super(message, e);
  }
}
