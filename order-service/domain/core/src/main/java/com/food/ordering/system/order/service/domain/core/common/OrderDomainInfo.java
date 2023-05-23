package com.food.ordering.system.order.service.domain.core.common;

public interface OrderDomainInfo {

  String ZONED_OF_ID = "UTC";
  String INITIAL_ORDER_INVALID_MSG = "Order isn't in correct state for initialization!";
  String INITIAL_PRICE_INVALID_MSG = "Total Price needs a value greater than ZERO!";

  String ORDER_ITEM_INVALID_PRICE = "Order item price %s isn't valid for product %s";

  String TOTAL_PRICE_INVALID_MSG = "The multiplied Item's Quantity by Item Price($%s) is not equal to Sub Total($%s)!";
  //
  String ORDER_STATE_PAY_INVALID = "Order isn't in correct state for pay operation!";
  String ORDER_STATE_APPROVE_INVALID = "Order isn't in correct state for approve operation!";
  String ORDER_STATE_INIT_CANCEL_INVALID = "Order isn't in correct state for init cancel operation!";
  String ORDER_STATE_CANCEL_INVALID = "Order isn't in correct state for cancel operation!";

  String RESTAURANT_STATE_INVALID = "Restaurant with id %s is current not active";

  String CREATE_ORDER_SUCCESS = "Order Successfully created";

}
