package com.food.ordering.system.order.service.domain.core.common;

public interface OrderDomainInfo {

  String ZONED_OF_ID = "UTC";
  String INITIAL_ORDER_INVALID_MSG = "Order isn't in correct state for initialization!";
  String INITIAL_PRICE_INVALID_MSG = "Total Price needs a value greater than ZERO!";
  String TOTAL_PRICE_INVALID_MSG = "Total price %s is not equal to Order items total %s!";
  String ITEM_PRICE_INVALID_MSG = "Order item price: %s is not valid for product %s";
  String ORDER_STATE_PAY_INVALID = "Order isn't in correct state for pay operation!";
  String ORDER_STATE_APPROVE_INVALID = "Order isn't in correct state for approve operation!";
  String ORDER_STATE_INIT_CANCEL_INVALID = "Order isn't in correct state for init cancel operation!";
  String ORDER_STATE_CANCEL_INVALID = "Order isn't in correct state for cancel operation!";

  String CUSTOMER_ID_NOT_FOUND = "Couldn't find customer with id: ${header.customerId}";

  String RESTAURANT_STATE_INVALID = "Restaurant with id %s is current not active";

}
