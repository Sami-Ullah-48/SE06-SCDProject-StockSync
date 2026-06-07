-- Initialize the database instance
USE stock_sync_db;

-- -- 1. Create Suppliers Table
-- CREATE TABLE IF NOT EXISTS suppliers (
--     supplier_id INT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(20) NOT NULL,
--     contact_phone VARCHAR(20)
-- );

-- -- 2. Create Categories Table
-- CREATE TABLE IF NOT EXISTS categories (
--     category_id INT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(20) NOT NULL
-- );
-- Drop Table IF EXISTS inventory_log;

-- -- 3. Create Polymorphic Products Table (Supports Abstract/Inheritance mapping)
-- DROP TABLE IF EXISTS products;

-- CREATE TABLE IF NOT EXISTS products (
--     id INT AUTO_INCREMENT PRIMARY KEY, 
--     name VARCHAR(20) NOT NULL UNIQUE,
--     category VARCHAR(20) NOT NULL,
--     price DOUBLE NOT NULL,
--     quantity INT NOT NULL DEFAULT 0,
--     supplier_id INT,
--     FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL
-- );

-- -- 4. Create Inventory Log Table for tracking changes


-- CREATE TABLE IF NOT EXISTS inventory_log (
--     log_id INT AUTO_INCREMENT PRIMARY KEY,
--     product_id INT,
--     stock_change INT NOT NULL,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
-- );