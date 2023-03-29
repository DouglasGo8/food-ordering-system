CREATE EXTENSION "pgcrypto";
--
CREATE TABLE ORDERS
(
    ORDER_ID 			TEXT NOT NULL PRIMARY KEY,
    CUSTOMER_ID 		TEXT NOT NULL,
    RESTAURANT_ID 		TEXT NOT NULL,
    TRACKING_ID 		TEXT NOT NULL,
    PRICE 				NUMERIC(5,2) NOT NULL,
    ORDER_STATUS 		TEXT NOT NULL
);

CREATE TABLE ORDER_ADDRESS
(
    ADDRESS_ID 	TEXT PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    ORDER_ID	TEXT NOT NULL,
    STREET		TEXT NOT NULL,
    CITY		TEXT NOT NULL,
    POSTAL_CODE	TEXT NOT NULL,
    CONSTRAINT fk_Orders_Address FOREIGN KEY (ORDER_ID) REFERENCES ORDERS(ORDER_ID)
);

CREATE TABLE ORDER_ITEMS
(
    ORDER_ITEM_ID 	TEXT DEFAULT gen_random_uuid() NOT NULL,
    ORDER_ID 		TEXT NOT NULL,
    PRODUCT_ID 		TEXT NOT NULL,
    PRICE			NUMERIC(5, 2) NOT NULL,
    SUB_TOTAL		NUMERIC(5, 2) NOT NULL,
    PRIMARY KEY(ORDER_ITEM_ID, ORDER_ID),
    CONSTRAINT fk_Order_Items FOREIGN KEY (ORDER_ID) REFERENCES ORDERS(ORDER_ID)
);
--
CREATE OR REPLACE PROCEDURE insert_orders(
    -- ORDERS TABLE
    p_ORDER_ID          TEXT,
    p_CUSTOMER_ID       TEXT,
    p_RESTAURANT_ID     TEXT,
    p_TRACKING_ID       TEXT,
    p_PRICE             NUMERIC(5, 2),
    p_ORDER_STATUS      TEXT,
    -- ORDER ADDRESS TABLE
    p_STREET			TEXT,
    p_CITY				TEXT,
    p_POSTAL_CODE		TEXT,
    -- RETURN
    r_ORDER_ID out      TEXT
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO ORDERS
        (ORDER_ID, CUSTOMER_ID, RESTAURANT_ID, TRACKING_ID, PRICE, ORDER_STATUS)
    VALUES
        (p_ORDER_ID, p_CUSTOMER_ID, p_RESTAURANT_ID, p_TRACKING_ID, p_PRICE, p_ORDER_STATUS);
    --
    INSERT INTO ORDER_ADDRESS
        (ORDER_ID, STREET, CITY, POSTAL_CODE)
    VALUES
        (p_ORDER_ID, p_STREET, p_CITY, p_POSTAL_CODE);
    ---

    -- RETURNING p_ORDER_ID INTO r_ORDER_ID

    r_ORDER_ID:= p_ORDER_ID;

    COMMIT;
    --
END;
$$;
--
call insert_orders(
        'ec78b161-3899-4866-8753-886b84a8fbce',
        'a5da1c79-9bd5-46af-9a07-8c6be207e1d0',
        '6329409b-5987-4188-8cae-4499fee16f72',
        '26a14700-5caa-404c-8234-e7412626cc1a',
        44.3,
        'PENDING',
        'Robert Robertson, 1234 NW Bobcat Lane, St. Robert',
        'NEW YORK',
        'MO 65584-5678',
        null
    );
--
CREATE OR REPLACE PROCEDURE insert_order_items(
    p_ORDER_ID   TEXT,
    p_PRODUCT_ID TEXT,
    p_PRICE      NUMERIC(5, 2),
    p_SUB_TOTAL  NUMERIC(5,2)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO ORDER_ITEMS
        (ORDER_ID, PRODUCT_ID, PRICE, SUB_TOTAL)
    VALUES
        (p_ORDER_ID, p_PRODUCT_ID, p_PRICE, p_SUB_TOTAL);
    --
END;
$$;
--
call insert_order_items(
    'ec78b161-3899-4866-8753-886b84a8fbce',
    '20a44234-db9c-4672-9455-dea9be80377b',
    22.3,
    22.3);
--
CREATE TABLE CUSTOMER
(
    customerId   TEXT NOT NULL PRIMARY KEY,
    customerName TEXT NOT NULL
);

CREATE TABLE RESTAURANT
(
    restaurantId   TEXT NOT NULL PRIMARY KEY,
    restaurantName TEXT NOT NULL,
    isActive       BOOLEAN NOT NULL
);

CREATE TABLE PRODUCT
(
    productId   TEXT NOT NULL PRIMARY KEY,
    productName TEXT NOT NULL
);

CREATE TABLE REST_PROD_COMPOSED
(
    productId    TEXT NOT NULL,
    restaurantId TEXT NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (productId) REFERENCES PRODUCT (productId),
    CONSTRAINT fk_restaurant FOREIGN KEY (restaurantId) REFERENCES RESTAURANT (restaurantId)
);
--
insert into CUSTOMER (customerId, customerName)
values ('af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 'Joe');
insert into CUSTOMER (customerId, customerName)
values ('7b68d44f-0882-4309-b4db-06c5341156f1', 'Mary');
--
insert into RESTAURANT(restaurantId, restaurantName, isActive)
values ('199bef80-e67a-420b-b036-48e422d4ac99', 'DOM', TRUE);
insert into RESTAURANT(restaurantId, restaurantName, isActive)
values ('c8dfc68d-9269-45c2-b2d1-7e0d0aa3c57b', 'Osaka SP', TRUE);
--
insert into PRODUCT (productId, productName)
values ('2abe9212-506e-4794-afe2-75522a76a4b5', 'Coke Sugar');
insert into PRODUCT (productId, productName)
values ('3987d947-ab7a-4a46-af7f-8594d591f83f', 'Salmon Gold Black');
insert into PRODUCT (productId, productName)
values ('abd3dd67-433d-408f-85a0-631b80de3596', 'Rice Portion');

-- Osaka SP
insert into REST_PROD_COMPOSED (productId, restaurantId)
values ('3987d947-ab7a-4a46-af7f-8594d591f83f', 'c8dfc68d-9269-45c2-b2d1-7e0d0aa3c57b');
insert into REST_PROD_COMPOSED (productId, restaurantId)
values ('2abe9212-506e-4794-afe2-75522a76a4b5', 'c8dfc68d-9269-45c2-b2d1-7e0d0aa3c57b');
insert into REST_PROD_COMPOSED (productId, restaurantId)
values ('abd3dd67-433d-408f-85a0-631b80de3596', 'c8dfc68d-9269-45c2-b2d1-7e0d0aa3c57b');
-- DOM
insert into REST_PROD_COMPOSED (productId, restaurantId)
values ('2abe9212-506e-4794-afe2-75522a76a4b5', '199bef80-e67a-420b-b036-48e422d4ac99');
insert into REST_PROD_COMPOSED (productId, restaurantId)
values ('abd3dd67-433d-408f-85a0-631b80de3596', '199bef80-e67a-420b-b036-48e422d4ac99');
--
-- ONLY TEST Effect
select /*r.restaurantname,*/ p.productname
from product p
         join rest_prod_composed rp
              on p.productid = rp.productid
         join restaurant r
              on r.restaurantid = rp.restaurantid
where r.restaurantid = 'c8dfc68d-9269-45c2-b2d1-7e0d0aa3c57b';
--'199bef80-e67a-420b-b036-48e422d4ac99';

--

CREATE OR REPLACE FUNCTION get_customer_byId(id TEXT)
    RETURNS TABLE
            (
                customerId   TEXT,
                customerName TEXT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT c.customerid, c.customername
        FROM CUSTOMER c
        WHERE c.customerid = get_customer_byId.id;
END;
$$ LANGUAGE plpgsql;

--

CREATE OR REPLACE FUNCTION get_restaurant_byId(id TEXT)
    RETURNS TABLE
            (
                restaurantId   TEXT,
                restaurantName TEXT,
                isActive BOOLEAN
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT r.restaurantId, r.restaurantName, r.isActive
        FROM RESTAURANT r
        WHERE r.restaurantId = get_restaurant_byId.id;
END;
$$ LANGUAGE plpgsql;