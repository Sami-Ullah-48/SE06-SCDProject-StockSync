-- Initialize the database instance
CREATE DATABASE IF NOT EXISTS stock_sync_db;
USE stock_sync_db;

-- 1. Create Suppliers Table
CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_phone VARCHAR(20)
);

-- 2. Create Categories Table
CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- 3. Create Polymorphic Products Table (Supports Abstract/Inheritance mapping)
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_type VARCHAR(30) NOT NULL, -- Discriminator field: 'PERISHABLE' or 'DURABLE'
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    expiry_date DATE DEFAULT NULL,     -- Nullable attribute for Perishable instances
    warranty_months INT DEFAULT NULL,  -- Nullable attribute for Durable instances
    category_id INT,
    supplier_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL
);

-- 4. Create Inventory Log Table for tracking changes
CREATE TABLE IF NOT EXISTS inventory_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    stock_change INT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Insert sample records to facilitate testing in upcoming steps
INSERT INTO categories (name) VALUES ('Fresh Items'), ('Long-Term Goods');
INSERT INTO suppliers (name, contact_phone) VALUES ('Gujrat Logistics', '+923001112223'), ('Gujranwala Importers', '+923214445556');