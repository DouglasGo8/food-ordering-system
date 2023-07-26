package com.food.ordering.system.shared.domain.exception;

public class DomainException extends RuntimeException {
  public DomainException(String message) {
    super(message);
  }

  public DomainException(String message, Throwable e) {
    super(message, e);
  }
}