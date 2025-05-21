/*
select * from tbl_customer
select * from tbl_products
select * from tbl_restaurant_products
select * from tbl_restaurants
----------------------------------
	-- truncate after tests --
----------------------------------
select * from tbl_orders
select * from tbl_order_items
select * from tbl_order_address

truncate table tbl_orders cascade
*/


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
WHERE r.id = 'd215b5f8-0249-4dc5-89a3-51fd148cfb45'
  AND p.id in ('d215b5f8-0249-4dc5-89a3-51fd148cfb47','d215b5f8-0249-4dc5-89a3-51fd148cfb48')