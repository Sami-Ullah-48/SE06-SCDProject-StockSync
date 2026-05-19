package com.stocksync.inventory.Service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import com.stocksync.inventory.Model.Product;
import com.stocksync.inventory.exception.InvalidProductException;
import com.stocksync.inventory.exception.InsufficientStockException;
import com.stocksync.inventory.Repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void addProductToInventory(Product product) throws InvalidProductException {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be blank.");
        }
        if (product.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be positive.");
        }
        if (product.getQuantity() < 0) {
            throw new InvalidProductException("Initial stock quantity cannot be negative.");
        }
        
        try {
            productRepository.save(product); // Clean Spring Data JPA save!
        } catch (Exception e) {
            // Catches database level issues like duplicate unique constraint violations
            throw new InvalidProductException("Rejection: A product with that name already exists!");
        }
    }

    public void adjustStockVolume(int id, int amountModifier) throws InvalidProductException, InsufficientStockException {
        // Safe, clean optional lookup using Spring Data mechanisms
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new InvalidProductException("Product profile ID " + id + " does not exist."));

        int targetQty = product.getQuantity() + amountModifier;
        if (targetQty < 0) {
            throw new InsufficientStockException("Operation rejected! Stock cannot drop below 0 units. Current stock: " + product.getQuantity());
        }

        product.setQuantity(targetQty);
        productRepository.save(product); // Automatically updates the existing row record
    }

}
