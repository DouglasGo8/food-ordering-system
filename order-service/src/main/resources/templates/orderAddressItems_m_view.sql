SELECT od.id    as order_id,
       od.customer_id,
       od.restaurant_id,
       od.tracking_id,
       od.price as order_total_price,
       od.order_status,
       od.failure_messages,
       oi.id    as order_item_id,
       oi.product_id,
       oi.price as order_item_price,
       oi.quantity,
       oi.sub_total,
       oa.id    as order_address_id,
       oa.city,
       oa.street,
       oa.postal_code
FROM tbl_orders od
         JOIN
     tbl_order_address oa
     ON od.id = oa.order_id
         JOIN
     tbl_order_items oi
     ON
         oi.order_id = od.id
WHERE od.id = :#${body.orderId}