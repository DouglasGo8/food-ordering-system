SELECT re1_0.product_id,
       re1_0.restaurant_id,
       re1_0.product_available,
       re1_0.product_name,
       re1_0.product_price,
       re1_0.restaurant_active,
       re1_0.restaurant_name
FROM order_restaurant_m_view re1_0
WHERE re1_0.restaurant_id = :#${body.id.value.toString}
  AND re1_0.product_id IN (:#in:ids)