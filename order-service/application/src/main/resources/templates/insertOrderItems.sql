insert_tbl_order_items(
    VARCHAR ${exchangeProperty.orderItemId},
    VARCHAR ${exchangeProperty.orderIdOut},
    VARCHAR ${body.productId},
    NUMERIC ${body.price},
    INTEGER ${body.quantity},
    NUMERIC ${body.subTotal}
)