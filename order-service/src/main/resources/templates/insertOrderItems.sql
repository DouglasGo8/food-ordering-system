insert_tbl_order_items(
    VARCHAR ${exchangeProperty.orderCreatedEvent.order.items[${exchangeProperty.CamelSplitIndex}].id.value},
    VARCHAR ${exchangeProperty.orderIdOut},
    VARCHAR ${body.productId},
    NUMERIC ${body.price},
    INTEGER ${body.quantity},
    NUMERIC ${body.subTotal}
)