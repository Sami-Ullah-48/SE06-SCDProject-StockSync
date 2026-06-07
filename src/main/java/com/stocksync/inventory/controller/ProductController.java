package com.stocksync.inventory.controller;


import com.stocksync.inventory.exception.InvalidProductException;
import com.stocksync.inventory.model.Product;
import com.stocksync.inventory.repository.ProductRepository;
import com.stocksync.inventory.service.ProductService;
import com.stocksync.inventory.exception.InsufficientStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products") // Sets the base URL for all endpoints
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // 1. CREATE: Add a brand new stock item profile
    // @PostMapping
    // public ResponseEntity<String> createProduct(@RequestBody Product product) {
    //     try {
    //         productService.addProductToInventory(product);
    //         return new ResponseEntity<>("Product created successfully via Web API!", HttpStatus.CREATED);
    //     } catch (InvalidProductException e) {
    //         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    //     }
    // }

    // 1. CREATE / UPSERT: Dynamic endpoint checking for name truncation and duplicate merging
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        try {
            boolean isNewProduct = productService.addProductToInventory(product);
            
            if (isNewProduct) {
                return new ResponseEntity<>("Product created successfully via Web API!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Duplicate entry found. Existing stock profile updated & merged successfully!", HttpStatus.OK);
            }
        } catch (InvalidProductException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected system error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. READ: Fetch all products in inventory
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 3. READ SINGLE: Fetch one product by its unique ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        return productRepository.findById(id)
                .<ResponseEntity<?>>map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>("Product ID not found.", HttpStatus.NOT_FOUND));
    }

    // 4. UPDATE: Adjust stock volumes (+ or - changes)
    @PatchMapping("/{id}/stock")
    public ResponseEntity<String> adjustStock(@PathVariable int id, @RequestParam int modifier) {
        try {
            productService.adjustStockVolume(id, modifier);
            return new ResponseEntity<>("Stock adjusted successfully!", HttpStatus.OK);
        } catch (InvalidProductException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InsufficientStockException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 5. DELETE: Completely drop a profile from inventory
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        if (!productRepository.existsById(id)) {
            return new ResponseEntity<>("Product ID does not exist.", HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
        return new ResponseEntity<>("Product profile purged successfully.", HttpStatus.OK);
    }
}
