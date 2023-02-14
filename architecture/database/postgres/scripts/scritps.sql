CREATE OR REPLACE FUNCTION get_customer_byId(id INTEGER)
RETURNS TABLE (customerid int, customername TEXT) AS $$
BEGIN
    RETURN QUERY
    SELECT c.customerid, c.customername
    FROM customer c
    WHERE c.customerid = get_customer_byId.id;
END;
$$ LANGUAGE plpgsql;