--
select * from tbl_restaurants
select * from tbl_order_approval

select * from tbl_orders
select * from tbl_order_items

select * from tbl_credit_entry;
select * from tbl_credit_history;


truncate table tbl_credit_entry;
truncate table tbl_credit_history;
truncate table tbl_orders cascade;
truncate table tbl_payments;
truncate table tbl_order_approval


INSERT INTO tbl_credit_entry(id, customer_id, total_credit_amount)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb21', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 100000.00);

INSERT INTO tbl_credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb23', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 100000.00, 'CREDIT');

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


select * from findTrackingById_fn('af20558e-5e77-4a6e-bb2f-fef1f14c0ee9')




select * from tbl_credit_entry
select * from tbl_credit_history where customer_id = 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9'





INSERT INTO tbl_credit_entry(id, customer_id, total_credit_amount)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb21', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 650.12);


INSERT INTO tbl_credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb24', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 600.12, 'CREDIT');

INSERT INTO tbl_credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb25', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 50.00, 'CREDIT');


