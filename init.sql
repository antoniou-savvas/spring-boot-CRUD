-- Create the necessary tables
CREATE TABLE IF NOT EXISTS customer (
    customer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    middle_name VARCHAR(255),
    last_name VARCHAR(255),
    address TEXT,
    phone VARCHAR(20),
    account_balance DECIMAL(15, 2)
);

CREATE TABLE IF NOT EXISTS product (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    model_no VARCHAR(255),
    description TEXT,
    price DECIMAL(15, 2)
);

CREATE TABLE IF NOT EXISTS invoice (
    invoice_id SERIAL PRIMARY KEY,
    date_created DATE,
    customer_id INT,
    total_amount DECIMAL(15, 2),
    CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE IF NOT EXISTS invoice_item (
    id SERIAL PRIMARY KEY,
    invoice_id INT,
    product_id INT,
    product_name VARCHAR(255),
    quantity INT,
    amount DECIMAL(15, 2),
    CONSTRAINT fk_invoice FOREIGN KEY(invoice_id) REFERENCES invoice(invoice_id),
    CONSTRAINT fk_product FOREIGN KEY(product_id) REFERENCES product(product_id)
);

-- Insert initial products if they do not exist
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM product WHERE product_id = 1000) THEN
        INSERT INTO product (name, model_no, description, price) VALUES 
        ('Kamado 1000', '1000', 'Kamado 1000', 199);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM product WHERE product_id = 2000) THEN
        INSERT INTO product (name, model_no, description, price) VALUES 
        ('Kamado 2000', '2000', 'Kamado 2000', 299);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM product WHERE product_id = 3000) THEN
        INSERT INTO product (name, model_no, description, price) VALUES 
        ('Kamado 3000', '3000', 'Kamado 3000', 399);
    END IF;
END $$;
