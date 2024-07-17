SELECT * FROM order_address_orderItems_m_view o
WHERE o.order_id = :#${body.orderId}