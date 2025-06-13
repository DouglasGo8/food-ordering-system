truncate table tbl_credit_entry;
truncate table tbl_credit_history;
truncate table tbl_orders cascade;
truncate table tbl_payments;
truncate table tbl_order_approval;

commit;


--select * from tbl_orders

--select * from tbl_products

--select * from tbl_restaurant_products


INSERT INTO tbl_credit_entry(id, customer_id, total_credit_amount)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb21', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 100000.00);

INSERT INTO tbl_credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb23', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 100000.00, 'CREDIT');

commit;


