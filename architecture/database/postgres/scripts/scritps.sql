--CREATE EXTENSION "pgcrypto";
--
DROP TABLE if EXISTS tbl_orders CASCADE;
--
CREATE TABLE tbl_orders
(
    id               TEXT           NOT NULL,
    customer_id      TEXT           NOT NULL,
    tracking_id      TEXT           NOT NULL,
    price            NUMERIC(10, 2) NOT NULL,
    order_status     TEXT           NOT NULL,
    failure_messages TEXT           NULL,
    CONSTRAINT tbl_orders_pkey PRIMARY KEY (id)
);
--
DROP TABLE if EXISTS tbl_order_items CASCADE;
--
CREATE TABLE tbl_order_items
(
    id         TEXT           NOT NULL,
    order_id   TEXT           NOT NULL,
    product_id TEXT           NOT NULL,
    price      NUMERIC(10, 2) NOT NULL,
    quantity   INTEGER        NOT NULL,
    sub_total  NUMERIC(10, 2) NOT NULL,
    CONSTRAINT tbl_order_items_pkey PRIMARY KEY (id, order_id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id)
            REFERENCES tbl_orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);
--
DROP TABLE if EXISTS tbl_order_address CASCADE;
--
CREATE TABLE tbl_order_address
(
    id          TEXT NOT NULL,
    order_id    TEXT NOT NULL,
    street      TEXT NOT NULL,
    postal_code TEXT NOT NULL,
    city        TEXT NOT NULL,
    CONSTRAINT tbl_order_address_pkey PRIMARY KEY (id, order_id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id)
        REFERENCES tbl_orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);
--
DROP TABLE if EXISTS tbl_customer CASCADE;
--
CREATE TABLE tbl_customer
(
    id         TEXT NOT NULL,
    user_name  TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL,
    CONSTRAINT tbl_customer_pkey PRIMARY KEY (id)
);
--
DROP TABLE if EXISTS tbl_restaurants CASCADE;
--
CREATE TABLE tbl_restaurants
(
    id     TEXT    NOT NULL,
    name   TEXT    NOT NULL,
    active BOOLEAN NOT NULL,
    CONSTRAINT tbl_restaurants_pkey PRIMARY KEY (id)
);
--
DROP TABLE if EXISTS tbl_order_approval CASCADE;
--
CREATE TABLE tbl_order_approval
(
    id            TEXT NOT NULL,
    restaurant_id TEXT NOT NULL,
    order_id      TEXT NOT NULL,
    status        TEXT NOT NULL,
    CONSTRAINT order_approval_pkey PRIMARY KEY (id)
);
--
DROP TABLE if EXISTS tbl_products CASCADE;
--
CREATE TABLE tbl_products
(
    id        TEXT           NOT NULL,
    name      TEXT           NOT NULL,
    price     NUMERIC(10, 2) NOT NULL,
    available BOOLEAN        NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY (id)
);
--
DROP TABLE if EXISTS tbl_restaurant_products CASCADE;
--
CREATE TABLE tbl_restaurant_products
(
    id            TEXT NOT NULL,
    restaurant_id TEXT NOT NULL,
    product_id    TEXT NOT NULL,
    CONSTRAINT restaurant_products_pkey PRIMARY KEY (id),
    CONSTRAINT fk_restaurant_id FOREIGN KEY (restaurant_id)
        REFERENCES tbl_restaurants (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT,
    CONSTRAINT fk_product_id FOREIGN KEY (product_id)
        REFERENCES tbl_products (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);
--
DROP MATERIALIZED VIEW if EXISTS order_restaurant_mview;
--
-- Materialized view used
/*CREATE MATERIALIZED VIEW order_restaurant_mview tablespace pg_default
AS
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
WHERE r.id = rp.restaurant_id
  AND p.id = rp.product_id
WITH DATA;
--
REFRESH MATERIALIZED VIEW order_restaurant_mview;
--
DROP MATERIALIZED VIEW if EXISTS order_customer_mview;
--
CREATE MATERIALIZED VIEW order_customer_mview
AS
SELECT id, user_name, first_name, last_name
FROM tbl_customer
WITH DATA;
--
-- Materialized view not used
REFRESH MATERIALIZED VIEW order_customer_mview;*/
--
/*DROP function IF EXISTS refresh_order_restaurant_mview;
--
CREATE OR replace function refresh_order_restaurant_mview()
    returns trigger
AS
'
    BEGIN
        refresh materialized VIEW order_restaurant_mview;
        return null;
    END;
' LANGUAGE plpgsql;
--*/
/*DROP trigger IF EXISTS refresh_order_restaurant_mview ON tbl_restaurant_products;
--
CREATE trigger refresh_order_restaurant_mview
    after INSERT OR UPDATE OR DELETE OR truncate
    ON tbl_restaurant_products
    FOR each statement
EXECUTE PROCEDURE refresh_order_restaurant_mview();*/
--
insert into tbl_customer (id, user_name, first_name, last_name)
values ('af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 'Joe', 'Joe', 'Doe');
insert into tbl_customer (id, user_name, first_name, last_name)
values ('7b68d44f-0882-4309-b4db-06c5341156f1', 'Mary', 'Mary', 'Page');
--
INSERT INTO tbl_restaurants(id, name, active)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb45', 'restaurant_1', TRUE);
INSERT INTO tbl_restaurants(id, name, active)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb46', 'restaurant_2', FALSE);
--
INSERT INTO tbl_products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb47', 'product_1', 22.76, FALSE);
INSERT INTO tbl_products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb48', 'product_2', 77.14, TRUE);
INSERT INTO tbl_products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb49', 'product_3', 20.00, FALSE);
INSERT INTO tbl_products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb50', 'product_4', 40.00, TRUE);
--
INSERT INTO tbl_restaurant_products(id, restaurant_id, product_id)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb51', 'd215b5f8-0249-4dc5-89a3-51fd148cfb45',
        'd215b5f8-0249-4dc5-89a3-51fd148cfb47');
INSERT INTO tbl_restaurant_products(id, restaurant_id, product_id)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb52', 'd215b5f8-0249-4dc5-89a3-51fd148cfb45',
        'd215b5f8-0249-4dc5-89a3-51fd148cfb48');
INSERT INTO tbl_restaurant_products(id, restaurant_id, product_id)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb53', 'd215b5f8-0249-4dc5-89a3-51fd148cfb46',
        'd215b5f8-0249-4dc5-89a3-51fd148cfb49');
INSERT INTO tbl_restaurant_products(id, restaurant_id, product_id)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb54', 'd215b5f8-0249-4dc5-89a3-51fd148cfb46',
        'd215b5f8-0249-4dc5-89a3-51fd148cfb50');
--
DROP procedure if EXISTS insert_tbl_orders;
--
CREATE OR REPLACE PROCEDURE insert_tbl_orders(
    -- tbl_orders
    p_order_id TEXT,
    p_customer_id TEXT,
    p_tracking_id TEXT,
    p_price NUMERIC(10, 2),
    p_order_status TEXT,
    p_failure_messages TEXT,
    -- tbl_order_address
    p_address_id TEXT,
    p_street TEXT,
    p_city TEXT,
    p_postal_code TEXT,
    -- RETURN
    pout_order_id OUT TEXT
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO tbl_orders (id, customer_id, tracking_id, price, order_status, failure_messages)
    VALUES (p_order_id, p_customer_id, p_tracking_id, p_price, p_order_status, p_failure_messages);
    --
    INSERT INTO tbl_order_address (id, order_id, street, city, postal_code)
    VALUES (p_address_id, p_order_id, p_street, p_city, p_postal_code);

    -- RETURNING p_order_id INTO pout_order_id

    pout_order_id := p_order_id;
    --COMMIT;
    --
END;
$$;
--
call insert_tbl_orders(
        'ec78b161-3899-4866-8753-886b84a8fbce',
        'a5da1c79-9bd5-46af-9a07-8c6be207e1d0',
        '6329409b-5987-4188-8cae-4499fee16f72',
        44.3,
        'PENDING',
        '',
        '13a9a038-a92b-424a-8c18-e7f82b018d68',
        '4 N. Talbot Lane New York',
        'New York',
        '10040',
        null
    );
--
DROP procedure if EXISTS insert_tbl_order_items;
--
CREATE OR REPLACE PROCEDURE insert_tbl_order_items(
    p_order_item_id TEXT,
    p_order_id TEXT,
    p_product_id TEXT,
    p_price NUMERIC(10, 2),
    p_quantity INTEGER,
    p_sub_total NUMERIC(10, 2)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO tbl_order_items (id, order_id, product_id, quantity, price, sub_total)
    VALUES (p_order_item_id, p_order_id, p_product_id, p_quantity, p_price, p_sub_total);
    --
END;
$$;
--
call insert_tbl_order_items(
        '1',
        'ec78b161-3899-4866-8753-886b84a8fbce',
        '20a44234-db9c-4672-9455-dea9be80377b',
        22.3,
        1,
        22.3);
--
DROP function if EXISTS find_restaurant_byId;
--
CREATE OR REPLACE FUNCTION find_restaurant_byId(p_id TEXT)
    RETURNS TABLE
            (
                id     TEXT,
                name   TEXT,
                active BOOLEAN
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT t.id, t.name, t.active
        FROM tbl_restaurants t
        WHERE t.id = find_restaurant_byId.p_id;
END;
$$ LANGUAGE plpgsql;
--
DROP function if EXISTS find_customer_byId;
--
CREATE OR REPLACE FUNCTION find_customer_byId(p_id TEXT)
    RETURNS TABLE
            (
                id TEXT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT t.id
        FROM tbl_customer t
        WHERE t.id = find_customer_byId.p_id;
END;
$$ LANGUAGE plpgsql;
--
DROP function if EXISTS refresh_order_customer_mview;
--
CREATE OR replace function refresh_order_customer_mview()
    returns trigger
AS
'
    BEGIN
        refresh materialized VIEW order_customer_mview;
        return null;
    END;
' LANGUAGE plpgsql;
--
CREATE TRIGGER refresh_order_customer_mview
    AFTER INSERT OR UPDATE OR DELETE OR truncate
    ON tbl_customer
    FOR each statement
EXECUTE PROCEDURE refresh_order_customer_mview();
--
-- FUNCTION findRestaurantInformation(idRestaurant)??
--
-- FUNCTION findByTrackingId(trackingId)??
-- DROP function if EXISTS find_tracking_byId;
--
/*CREATE OR REPLACE FUNCTION find_tracking_byId(p_id TEXT)
    RETURNS TABLE
            (
                customer_id      TEXT,
                tracking_id      TEXT,
                price            NUMERIC(10, 2),
                order_status     TEXT,
                failure_messages TEXT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT o.price
        FROM tbl_orders o
        WHERE o.tracking_id = find_tracking_byId.p_id;
END;
$$ LANGUAGE plpgsql;*/