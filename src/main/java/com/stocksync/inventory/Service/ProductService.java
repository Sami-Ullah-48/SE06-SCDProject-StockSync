package com.stocksync.inventory.Service;

import com.stocksync.inventory.Model.Product;
import com.stocksync.inventory.exception.InvalidProductException;
import com.stocksync.inventory.exception.InsufficientStockException;
import com.stocksync.inventory.Repository.ProductDAO;
public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    public void addProductToInventory(Product product) throws InvalidProductException {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be blank or empty.");
        }
        if (product.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be greater than zero.");
        }
        if (product.getQuantity() < 0) {
            throw new InvalidProductException("Initial stock quantity cannot be negative.");
        }
        productDAO.saveProduct(product);
    }

    public void adjustStockVolume(int id, int amountModifier) throws InvalidProductException, InsufficientStockException {
        int currentQty = productDAO.getCurrentQuantity(id);
        if (currentQty == -1) {
            throw new InvalidProductException("Product profile ID " + id + " does not exist in inventory.");
        }

        int targetQty = currentQty + amountModifier;
        if (targetQty < 0) {
            throw new InsufficientStockException("Operation rejected! Stock cannot drop below 0 units. Current stock: " + currentQty);
        }

        productDAO.updateProductQuantity(id, targetQty);
    }
    // public void displayInventory() {
    //     productDAO.printAllProducts();
    // }
    // public void addStockVolume(int id, int amountToAdd) {
    //     int currentQty = productDAO.getCurrentQuantity(id);
    //     if (currentQty == -1) {
    //         System.out.println("SERVICE ERROR: Product does not exist.");
    //         return;
    //     }
    //     if (amountToAdd <= 0) {
    //         System.out.println("VALIDATION ERROR: Addition volume must be greater than zero!");
    //         return;
    //     }
    //     productDAO.updateProductQuantity(id, currentQty + amountToAdd);
    // }
    // public void subtractStockVolume(int id, int amountToSubtract) {
    //     int currentQty = productDAO.getCurrentQuantity(id);
    //     if (currentQty == -1) {
    //         System.out.println("SERVICE ERROR: Product does not exist.");
    //         return;
    //     }
    //     if (amountToSubtract <= 0) {
    //         System.out.println("VALIDATION ERROR: Subtraction volume must be greater than zero!");
    //         return;
    //     }
    //     if (currentQty - amountToSubtract < 0) {
    //         System.out.println("VALIDATION ERROR: Insufficient stock inventory! Cannot subtract below 0.");
    //         return;
    //     }
    //     productDAO.updateProductQuantity(id, currentQty - amountToSubtract);
    // }
    // public void updateStockRaw(int id, int explicitQty) {
    //     if (explicitQty < 0) {
    //         System.out.println("VALIDATION ERROR: Total stock level cannot be negative!");
    //         return;
    //     }
    //     productDAO.updateProductQuantity(id, explicitQty);
    // }

    // public void removeProduct(int id) {
    //     productDAO.deleteProduct(id);
    // }

}
