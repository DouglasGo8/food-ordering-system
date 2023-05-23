insert_tbl_orders(
    VARCHAR ${exchangeProperty.orderCreatedEvent.order.id.value},
    VARCHAR ${body.customerId},
    VARCHAR ${exchangeProperty.orderCreatedEvent.order.trackingId.value},
    NUMERIC ${body.price},
    VARCHAR ${exchangeProperty.orderCreatedEvent.order.orderStatus},
    VARCHAR ${exchangeProperty.fail_msg},
    VARCHAR ${exchangeProperty.orderCreatedEvent.order.deliveryAddress.id},
    VARCHAR ${body.address.street},
    VARCHAR ${body.address.city},
    VARCHAR ${body.address.postalCode},
    OUT VARCHAR result
)