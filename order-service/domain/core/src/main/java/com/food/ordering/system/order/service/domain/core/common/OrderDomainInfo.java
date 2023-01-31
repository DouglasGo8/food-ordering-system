package com.food.ordering.system.order.service.domain.core.common;

import com.food.ordering.system.shared.domain.valueobject.BaseId;
import com.food.ordering.system.shared.domain.valueobject.Money;

import java.math.BigDecimal;

public interface OrderDomainInfo {
  String INITIAL_ORDER_INVALID_MSG = "Order isn't in correct state for initialization!";
  String INITIAL_PRICE_INVALID_MSG = "Total Price needs a value and greater than ZERO!";
  String TOTAL_PRICE_INVALID_MSG = "Total price %s is not equal to Order items total %s!";
  String ITEM_PRICE_INVALID_MSG = "Order item price: %s is not valid for product %s";
}
