package com.food.ordering.system.common.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 *
 * @param <T>
 */
@Getter
@EqualsAndHashCode
public abstract class BaseId<T> {
  private final T value;
  protected BaseId(T value) {
    this.value = value;
  }
}
