insert_tbl_orders(
    VARCHAR ${body.id.value},
    VARCHAR ${body.customerId.value},
    VARCHAR ${body.restaurantId.value},
    VARCHAR ${body.trackingId.value},
    NUMERIC ${body.price.amount},
    VARCHAR ${body.orderStatus},
    VARCHAR ${exchangeProperty.fail_msg},
    VARCHAR ${body.deliveryAddress.id},
    VARCHAR ${body.deliveryAddress.street},
    VARCHAR ${body.deliveryAddress.city},
    VARCHAR ${body.deliveryAddress.postalCode},
    OUT VARCHAR result
)