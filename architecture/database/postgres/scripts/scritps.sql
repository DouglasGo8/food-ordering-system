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