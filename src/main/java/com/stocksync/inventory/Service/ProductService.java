package com.stocksync.inventory.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import com.stocksync.inventory.exception.InvalidProductException;
import com.stocksync.inventory.model.Product;
import com.stocksync.inventory.repository.ProductRepository;
import com.stocksync.inventory.exception.InsufficientStockException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public boolean addProductToInventory(Product product) throws InvalidProductException {
    if (product.getName() == null || product.getName().trim().isEmpty()) {
        throw new InvalidProductException("Product name cannot be blank.");
    }

    String rawName = product.getName().trim();
    if (rawName.length() > 20) {
        System.out.println("[⚠️ NOTICE] Name exceeds 20 characters. Truncating automatically...");
        rawName = rawName.substring(0, 20);
    }
    product.setName(rawName);

    if (product.getPrice() <= 0) {
        throw new InvalidProductException("Product price must be positive.");
    }
    if (product.getQuantity() < 0) {
        throw new InvalidProductException("Initial stock quantity cannot be negative.");
    }
    
    Optional<Product> existingProductOpt = productRepository.findByName(product.getName());
    
    if (existingProductOpt.isPresent()) {
        Product existingProduct = existingProductOpt.get();
        existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setSupplier(product.getSupplier());
        
        productRepository.save(existingProduct);
        return false; // Returns false indicating an UPDATE/MERGE took place
    } else {
        productRepository.save(product);
        return true;  // Returns true indicating a NEW profile was created
    }
}


    // public void addProductToInventory(Product product) throws InvalidProductException {
    //     if (product.getName() == null || product.getName().trim().isEmpty()) {
    //         throw new InvalidProductException("Product name cannot be blank.");
    //     }
    //     String rawName = product.getName().trim();
    //     if (rawName.length() > 20) {
    //         System.out.println("[⚠️ NOTICE] Name exceeds 20 characters. Truncating automatically...");
    //         rawName = rawName.substring(0, 20); // Cuts off everything past index 19
    //     }
    //     product.setName(rawName);
        
    //     if (product.getPrice() <= 0) {
    //         throw new InvalidProductException("Product price must be positive.");
    //     }
    //     if (product.getQuantity() < 0) {
    //         throw new InvalidProductException("Initial stock quantity cannot be negative.");
    //     }
        
    //     try {
    //         productRepository.save(product); // Clean Spring Data JPA save!
    //     } catch (Exception e) {
    //         // Catches database level issues like duplicate unique constraint violations
    //         throw new InvalidProductException("Rejection: A product with that name already exists!");
    //     }
    // }

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
