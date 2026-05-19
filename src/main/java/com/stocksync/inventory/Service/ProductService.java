package com.stocksync.inventory.Serivce;

import com.stocksync.inventory.Model.Product;
import com.stocksync.inventory.Repository.ProductDAO;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    public void addProductToInventory(Product product) {
        if (product.name == null || product.name.trim().isEmpty()) {
            System.out.println("VALIDATION ERROR: Product name cannot be blank!");
            return;
        }
        if (product.price <= 0) {
            System.out.println("VALIDATION ERROR: Price must be positive!");
            return;
        }
        if (product.quantity < 0) {
            System.out.println("VALIDATION ERROR: Stock count cannot be negative!");
            return;
        }
        productDAO.saveProduct(product);
    }
    public void displayInventory() {
        productDAO.printAllProducts();
    }
    public void addStockVolume(int id, int amountToAdd) {
        int currentQty = productDAO.getCurrentQuantity(id);
        if (currentQty == -1) {
            System.out.println("SERVICE ERROR: Product does not exist.");
            return;
        }
        if (amountToAdd <= 0) {
            System.out.println("VALIDATION ERROR: Addition volume must be greater than zero!");
            return;
        }
        productDAO.updateProductQuantity(id, currentQty + amountToAdd);
    }
    public void subtractStockVolume(int id, int amountToSubtract) {
        int currentQty = productDAO.getCurrentQuantity(id);
        if (currentQty == -1) {
            System.out.println("SERVICE ERROR: Product does not exist.");
            return;
        }
        if (amountToSubtract <= 0) {
            System.out.println("VALIDATION ERROR: Subtraction volume must be greater than zero!");
            return;
        }
        if (currentQty - amountToSubtract < 0) {
            System.out.println("VALIDATION ERROR: Insufficient stock inventory! Cannot subtract below 0.");
            return;
        }
        productDAO.updateProductQuantity(id, currentQty - amountToSubtract);
    }
    public void updateStockRaw(int id, int explicitQty) {
        if (explicitQty < 0) {
            System.out.println("VALIDATION ERROR: Total stock level cannot be negative!");
            return;
        }
        productDAO.updateProductQuantity(id, explicitQty);
    }

    public void removeProduct(int id) {
        productDAO.deleteProduct(id);
    }
    
}
