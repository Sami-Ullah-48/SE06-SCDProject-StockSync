package com.stocksync.inventory.Repository;
import com.stocksync.inventory.Model.Product;
import com.stocksync.inventory.Model.PerishableProduct;
import com.stocksync.inventory.Model.DurableProduct;

import java.sql.*;

public class ProductDAO {
    private String url = "jdbc:mysql://localhost:3306/stock_sync_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private String user = "root";
    private String password = "12345"; // Matches your verification password

    public void saveProduct(Product product) {
        String sql = "INSERT INTO products (product_type, name, price, quantity, expiry_date, warranty_months) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(2, product.name);
            stmt.setDouble(3, product.price);
            stmt.setInt(4, product.quantity);

            if (product instanceof PerishableProduct) {
                stmt.setString(1, "PERISHABLE");
                stmt.setString(5, ((PerishableProduct) product).expiryDate.toString());
                stmt.setNull(6, java.sql.Types.INTEGER);
            } else if (product instanceof DurableProduct) {
                stmt.setString(1, "DURABLE");
                stmt.setNull(5, java.sql.Types.DATE);
                stmt.setInt(6, ((DurableProduct) product).warrantyMonths);
            }

            stmt.executeUpdate();
            System.out.println("Product successfully written to MySQL tables via raw JDBC connection!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
