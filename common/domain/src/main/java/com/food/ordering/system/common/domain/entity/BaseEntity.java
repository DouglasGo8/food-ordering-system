package com.food.ordering.system.common.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @param <ID>
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class BaseEntity<ID> {
  private ID id;
}
