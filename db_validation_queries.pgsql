-- Check the structure of the database
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public';

-- Check the structure of the customer table
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'customer';

-- Check the structure of the product table
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'product';

-- Check the structure of the invoice table
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'invoice';

-- Check the structure of the invoice_item table
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'invoice_item';