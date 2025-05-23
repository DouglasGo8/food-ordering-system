--CREATE EXTENSION "pgcrypto";
--
DROP TABLE if EXISTS tbl_orders CASCADE;
--
CREATE TABLE tbl_orders
(
    id               TEXT           NOT NULL,
    customer_id      TEXT           NOT NULL,
    restaurant_id    TEXT           NOT NULL,
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
DROP TABLE IF EXISTS tbl_payments CASCADE;

CREATE TABLE tbl_payments
(
    id          TEXT           NOT NULL,
    customer_id TEXT           NOT NULL,
    order_id    TEXT           NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    created_at  TEXT           NOT NULL,
    status      TEXT           NOT NULL,
    CONSTRAINT payments_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS tbl_credit_entry CASCADE;

CREATE TABLE tbl_credit_entry
(
    id                  TEXT           NOT NULL,
    customer_id         TEXT           NOT NULL,
    total_credit_amount NUMERIC(10, 2) NOT NULL,
    CONSTRAINT credit_entry_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS tbl_credit_history CASCADE;

CREATE TABLE tbl_credit_history
(
    id          TEXT           NOT NULL,
    customer_id TEXT           NOT NULL,
    amount      NUMERIC(10, 2) NOT NULL,
    type        TEXT           NOT NULL,
    CONSTRAINT credit_history_pkey PRIMARY KEY (id)
);
--
DROP MATERIALIZED VIEW IF EXISTS order_restaurant_m_view;
--
CREATE MATERIALIZED VIEW order_restaurant_m_view
    TABLESPACE pg_default
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

REFRESH MATERIALIZED VIEW order_restaurant_m_view;

DROP function IF EXISTS refresh_order_restaurant_m_view;

CREATE OR replace function refresh_order_restaurant_m_view()
    returns trigger
AS
'
    BEGIN
        refresh materialized VIEW order_restaurant_m_view;
        return null;
    END;
' LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_restaurant_m_view ON tbl_restaurant_products;

CREATE trigger refresh_order_restaurant_m_view
    after INSERT OR UPDATE OR DELETE OR truncate
    ON tbl_restaurant_products
    FOR each statement
EXECUTE PROCEDURE refresh_order_restaurant_m_view();
--
DROP MATERIALIZED VIEW IF EXISTS order_address_orderItems_m_view;
--
CREATE MATERIALIZED VIEW order_address_orderItems_m_view
    TABLESPACE pg_default
AS
SELECT
    od.id as order_id,
    od.customer_id,
    od.restaurant_id,
    od.tracking_id,
    od.price as order_total_price,
    od.order_status,
    od.failure_messages,
    oi.id as order_item_id,
    oi.product_id,
    oi.price as order_item_price,
    oi.quantity,
    oi.sub_total,
    oa.id as order_address_id,
    oa.city,
    oa.street,
    oa.postal_code
FROM
    tbl_orders od
        JOIN
    tbl_order_address oa
    ON od.id = oa.order_id
        JOIN
    tbl_order_items oi
    ON
        oi.order_id = od.id
WITH DATA;
--
REFRESH MATERIALIZED VIEW order_address_orderItems_m_view;
--
DROP function IF EXISTS refresh_order_address_orderItems_m_view;
--
CREATE OR replace function refresh_order_address_orderItems_m_view()
    returns trigger
AS
'
    BEGIN
        refresh materialized VIEW order_address_orderItems_m_view;
        return null;
    END;
' LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_address_orderItems_m_view ON tbl_orders;

CREATE trigger refresh_order_address_orderItems_m_view
    after INSERT OR UPDATE OR DELETE OR truncate
    ON tbl_orders
    FOR each statement
EXECUTE PROCEDURE refresh_order_address_orderItems_m_view();
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
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb47', 'product_1', 50.00, TRUE);
INSERT INTO tbl_products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb48', 'product_2', 50.00, TRUE);
INSERT INTO tbl_products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb49', 'product_3', 50.00, FALSE);
INSERT INTO tbl_products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb50', 'product_4', 50.00, TRUE);
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
INSERT INTO tbl_credit_entry(id, customer_id, total_credit_amount)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb21', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 650.12);
-- History for Each Credit Entry
INSERT INTO tbl_credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb23', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 100.00, 'CREDIT');
INSERT INTO tbl_credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb24', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 600.12, 'CREDIT');
INSERT INTO tbl_credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb25', 'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9', 50.00, 'DEBIT');

INSERT INTO tbl_credit_entry(id, customer_id, total_credit_amount)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb22', '7b68d44f-0882-4309-b4db-06c5341156f1', 100.00);
INSERT INTO tbl_credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb26', '7b68d44f-0882-4309-b4db-06c5341156f1', 100.00, 'CREDIT');
--
DROP function if EXISTS find_order_byId(p_id TEXT);
--
CREATE OR REPLACE FUNCTION find_order_byId(p_id TEXT)
    RETURNS TABLE
            (
                id                  TEXT,
                customer_id         TEXT,
                restaurant_id       TEXT,
                tracking_id         TEXT,
                price               NUMERIC(10, 2),
                order_status        TEXT,
                failure_messages    TEXT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT o.*
        FROM tbl_orders o
        WHERE o.id = find_order_byId.p_id;
END;
$$ LANGUAGE plpgsql;
--
DROP PROCEDURE if EXISTS insert_tbl_orders;
--
-- needs be improved
CREATE OR REPLACE PROCEDURE insert_tbl_orders(
    -- tbl_orders
    p_order_id TEXT,
    p_customer_id TEXT,
    p_restaurant_id TEXT,
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
    INSERT INTO tbl_orders (id, customer_id, restaurant_id, tracking_id, price, order_status, failure_messages)
    VALUES (p_order_id, p_customer_id, p_restaurant_id,
            p_tracking_id, p_price,
            p_order_status, p_failure_messages)
    ON CONFLICT ON CONSTRAINT tbl_orders_pkey DO UPDATE SET order_status = p_order_status;
    --
    INSERT INTO tbl_order_address (id, order_id, street, city, postal_code)
    VALUES (p_address_id, p_order_id, p_street, p_city, p_postal_code)
    ON CONFLICT ON CONSTRAINT tbl_order_address_pkey DO NOTHING ;
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
        'd215b5f8-0249-4dc5-89a3-51fd148cfb45',
        '6329409b-5987-4188-8cae-4499fee16f72',
        38.68,
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
        'd215b5f8-0249-4dc5-89a3-51fd148cfb47',
        22.3,
        1,
        22.3);

call insert_tbl_order_items(
        '2',
        'ec78b161-3899-4866-8753-886b84a8fbce',
        'd215b5f8-0249-4dc5-89a3-51fd148cfb48',
        16.38,
        2,
        32.76);
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
DROP function if EXISTS find_restaurantMView_byId;
-- query based on commented view
--
DROP PROCEDURE if EXISTS insert_tbl_payment;
--
CREATE OR REPLACE PROCEDURE insert_tbl_payment(
    p_payment_id TEXT,
    p_customer_id TEXT,
    p_order_id TEXT,
    p_price NUMERIC(10, 2),
    p_created_at TEXT,
    p_status TEXT
)
    LANGUAGE plpgsql
AS
$$
-- BODY
BEGIN
    INSERT INTO tbl_payments(id, customer_id, order_id, price, created_at, status)
    VALUES (p_payment_id, p_customer_id, p_order_id,
            p_price, REPLACE(p_created_at, '[UTC]', ''), p_status);
END;
$$;
--
call insert_tbl_payment(
        '14a736f8-e381-4d9a-b617-5a8706a25c9b', -- payment_id
        'a5da1c79-9bd5-46af-9a07-8c6be207e1d0', -- customer_id
        'ec78b161-3899-4866-8753-886b84a8fbce', -- order_id
        22.3, -- price
        '2023-11-07T05:05:27.811042-03:00',
        'COMPLETED');
--
DROP PROCEDURE if EXISTS update_tbl_payment;
--
CREATE OR REPLACE PROCEDURE update_tbl_payment(
    p_payment_id TEXT,
    p_order_id TEXT,
    p_status TEXT
)
    LANGUAGE plpgsql
AS
$$
-- BODY
BEGIN
    UPDATE tbl_payments
    SET status = p_status
    WHERE id = p_payment_id
      AND order_id = p_order_id;
    --INSERT INTO tbl_payments(id, customer_id, order_id, price, created_at, status)
    --VALUES (p_payment_id, p_customer_id, p_order_id,
    --        p_price, REPLACE(p_created_at, '[UTC]', ''), p_status);
END;
$$;
--
DROP PROCEDURE if EXISTS insert_tbl_credit_entry;
--
CREATE OR REPLACE PROCEDURE insert_tbl_credit_entry(
    p_credit_entry_id TEXT,
    p_customer_id TEXT,
    p_total_credit_amount NUMERIC(10, 2)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO tbl_credit_entry(id, customer_id, total_credit_amount)
    VALUES (p_credit_entry_id, p_customer_id, p_total_credit_amount)
    --
    ON CONFLICT ON CONSTRAINT credit_entry_pkey DO UPDATE SET total_credit_amount = p_total_credit_amount;
END;
$$;
--
call insert_tbl_credit_entry(
        'd215b5f8-0249-4dc5-89a3-51fd148cfb21',
        'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9',
        637.36 --650.12
     );
--
DROP procedure if EXISTS insert_tbl_credit_history;
--
CREATE OR REPLACE PROCEDURE insert_tbl_credit_history(
    p_credit_history_id TEXT,
    p_customer_id TEXT,
    p_amount NUMERIC(10, 2),
    p_type TEXT
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO tbl_credit_history(id, customer_id, amount, type)
    VALUES (p_credit_history_id, p_customer_id, p_amount, p_type);
END;
$$;
--
call insert_tbl_credit_history('bf1202b6-c298-4b4c-b86b-6b24fd996049',
                               'af20558e-5e77-4a6e-bb2f-fef1f14c0ee9',
                               33.12,
                               'DEBIT');
--
DROP PROCEDURE if EXISTS insert_tbl_order_approval;
--
CREATE OR REPLACE PROCEDURE insert_tbl_order_approval(
    -- tbl_order_approval
    p_id TEXT,
    p_restaurant_id TEXT,
    p_order_id TEXT,
    p_status TEXT
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO tbl_order_approval (id, restaurant_id, order_id, status)
    VALUES (p_id, p_restaurant_id, p_order_id, p_status);
    --
    --COMMIT;
    --
END;
$$;
--
call insert_tbl_order_approval('88f9368e-a6e4-4aa0-8651-db351efe3f13',
                               'd215b5f8-0249-4dc5-89a3-51fd148cfb45',
                               'ec78b161-3899-4866-8753-886b84a8fbce',
                               'APPROVED');
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
DROP MATERIALIZED VIEW IF EXISTS order_customer_m_view;
--
CREATE MATERIALIZED VIEW order_customer_m_view
    TABLESPACE pg_default
AS
SELECT id,
       user_name,
       first_name,
       last_name
FROM tbl_customer
WITH DATA;
--
REFRESH MATERIALIZED VIEW order_customer_m_view;
--
DROP function IF EXISTS refresh_order_customer_m_view;
--
CREATE OR replace function refresh_order_customer_m_view()
    returns trigger
AS
'
    BEGIN
        REFRESH MATERIALIZED VIEW order_customer_m_view;
        return null;
    END;
' LANGUAGE plpgsql;
--
DROP trigger IF EXISTS refresh_order_customer_m_view ON tbl_customer;
--
CREATE trigger refresh_order_customer_m_view
    after INSERT OR UPDATE OR DELETE OR truncate
    ON tbl_customer
    FOR each statement
EXECUTE PROCEDURE refresh_order_customer_m_view();
--

--
DROP function if EXISTS findCustomerIdCreditEntry_fn;
--
CREATE OR REPLACE FUNCTION findCustomerIdCreditEntry_fn(p_id TEXT)
    RETURNS TABLE
            (
                id                  TEXT,
                customer_id         TEXT,
                total_credit_amount NUMERIC(10, 2)
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT t.id, t.customer_id, t.total_credit_amount
        FROM tbl_credit_entry t
        WHERE t.customer_id = findCustomerIdCreditEntry_fn.p_id;
END;
$$ LANGUAGE plpgsql;
---
DROP function if EXISTS findCustomerIdCreditHistory_fn;
---
CREATE OR REPLACE FUNCTION findCustomerIdCreditHistory_fn(p_id TEXT)
    RETURNS TABLE
            (
                id          TEXT,
                customer_id TEXT,
                amount      NUMERIC(10, 2),
                type        TEXT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT t.id, t.customer_id, t.amount, t.type
        FROM tbl_credit_history t
        WHERE t.customer_id = findCustomerIdCreditHistory_fn.p_id;
END;
$$ LANGUAGE plpgsql;
---
DROP function if EXISTS findPaymentByOrderId_fn;
---
CREATE OR REPLACE FUNCTION findPaymentByOrderId_fn(p_orderId TEXT)
    RETURNS TABLE
            (
                id          TEXT,
                customer_id TEXT,
                order_id    TEXT,
                price       NUMERIC(10, 2),
                created_at  TEXT,
                status      TEXT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT p.id,
               p.customer_id,
               p.order_id,
               p.price,
               p.created_at,
               p.status
        FROM tbl_payments p
        WHERE p.order_id = findPaymentByOrderId_fn.p_orderId;
END;
$$ LANGUAGE plpgsql;