SELECT r.id        AS restaurant_id,
       r.name      AS restaurant_name,
       r.active    AS restaurant_active,
       p.id        AS product_id,
       p.name      AS product_name,
       p.price     AS product_price,
       p.available AS product_available
FROM tbl_restaurants r,
     tbl_products p,
     tbl_restaurant_products rp
WHERE r.id = :#${body.restaurantId}
  AND p.id in (:#in:ids)