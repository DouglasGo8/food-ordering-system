package com.food.ordering.system.shared.domain;

public interface DomainConstants {
  String UTC = "UTC";

  String CREATE_ORDER_SUCCESS = "Order Successfully created";
  String RESTAURANT_STATE_INVALID = "Restaurant with id %s is current not active";
  String INITIAL_ORDER_INVALID_MSG = "Order isn't in correct state for initialization!";
  String INITIAL_PRICE_INVALID_MSG = "Total Price needs a value greater than ZERO!";
  String ORDER_ITEM_INVALID_PRICE = "Order item price %s isn't valid for product %s";
  String TOTAL_PRICE_INVALID_MSG = "The multiplied Item's Quantity by Item Price($%s) is not equal to Sub Total($%s)!";
  //
  String ORDER_STATE_PAY_INVALID = "Order isn't in correct state for pay operation!";
  String ORDER_STATE_CANCEL_INVALID = "Order isn't in correct state for cancel operation!";
  String ORDER_STATE_APPROVE_INVALID = "Order isn't in correct state for approve operation!";
  String ORDER_STATE_INIT_CANCEL_INVALID = "Order isn't in correct state for init cancel operation!";
  String CUSTOMER_NOT_FOUND_ID = "Could not find customer with customer id: ${exchangeProperty.customerId}";
  String RESTAURANT_INFO_NOT_FOUND = "Could not find restaurant with restaurant id: %s";


}
