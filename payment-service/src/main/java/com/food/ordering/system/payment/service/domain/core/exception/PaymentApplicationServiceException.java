package com.food.ordering.system.payment.service.domain.core.exception;

import com.food.ordering.system.shared.domain.exception.DomainException;

public class PaymentApplicationServiceException extends DomainException {

  public PaymentApplicationServiceException(String message) {
    super(message);
  }

  public PaymentApplicationServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
