insert_tbl_payment(
    VARCHAR ${body.id.value},
    VARCHAR ${body.customerId.value},
    VARCHAR ${body.orderId.value},
    NUMERIC ${body.price.amount},
    VARCHAR ${body.createdAt},
    VARCHAR ${body.paymentStatus}
)