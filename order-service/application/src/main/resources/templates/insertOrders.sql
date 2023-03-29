insert_orders(
    VARCHAR ${exchangeProperty.orderCreatedEvent.order.id.value},
    VARCHAR ${body.customerId},
    VARCHAR ${body.restaurantId},
    VARCHAR ${exchangeProperty.orderCreatedEvent.order.trackingId.value},
    NUMERIC ${body.price},
    VARCHAR ${exchangeProperty.orderCreatedEvent.order.orderStatus},
    VARCHAR ${body.address.street},
    VARCHAR ${body.address.postalCode},
    VARCHAR ${body.address.city},
    OUT VARCHAR result
)