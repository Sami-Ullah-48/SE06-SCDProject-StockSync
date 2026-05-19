package com.stocksync.inventory.Repository;
import com.stocksync.inventory.Model.Product;
import java.sql.*;

public class ProductDAO {
    private String url = "jdbc:mysql://localhost:3306/stock_sync_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private String user = "root";
    private String password = "Sami@mysql@001"; 

    public boolean saveProduct(Product product) {
        String sql = "INSERT INTO products (name, category, price, quantity, description, supplier) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.name);
            stmt.setString(2, product.category);
            stmt.setDouble(3, product.price);
            stmt.setInt(4, product.quantity);
            stmt.setString(5, product.description);
            stmt.setString(6, product.supplier);
            
            stmt.executeUpdate();
            System.out.println("Product saved successfully directly to MySQL database tables!");
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // 1062 is the MySQL error code for Duplicate Entry
                System.out.println("DATABASE REJECTION: A product with the name '" + product.name + "' already exists!");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void printAllProducts() {
        String sql = "SELECT * FROM products";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("\n=========================== CURRENT STOCK LEVEL INVENTORY ===========================");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                   " | Name: " + rs.getString("name") + 
                                   " | Cat: " + rs.getString("category") +
                                   " | Price: $" + rs.getDouble("price") + 
                                   " | Qty: " + rs.getInt("quantity") +
                                   " | Supp: " + rs.getString("supplier") +
                                   " | Desc: " + rs.getString("description"));
            }
            System.out.println("====================================================================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateProductQuantity(int id, int newQty) {
        String sql = "UPDATE products SET quantity = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newQty);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product ID " + id + " quantity count updated to " + newQty + " successfully!");
            } else {
                System.out.println("ERROR: Product record ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product ID " + id + " wiped out completely from system tables.");
            } else {
                System.out.println("ERROR: Product record ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getCurrentQuantity(int id) {
        String sql = "SELECT quantity FROM products WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Product not found code flag
    }
}
