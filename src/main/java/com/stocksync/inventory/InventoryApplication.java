package com.stocksync.inventory;

import com.stocksync.inventory.Model.*;
//import com.stocksync.inventory.Repository.ProductDAO;
import com.stocksync.inventory.Serivce.ProductService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class InventoryApplication implements CommandLineRunner {

	public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ProductService productService = new ProductService();
       // ProductService productService = new ProductService();
        Scanner scanner = new Scanner( System.in); 
        // Note: keeping standard System.in reference basic
        
        System.out.println("=== Welcome to Stock-Sync Engine ===");
        
        while (true) {
            System.out.println("\n--- INVENTORY OPERATIONS MENU ---");
            System.out.println("1. Add Perishable Product");
            System.out.println("2. Add Durable Product");
            System.out.println("3. Exit Application");
            System.out.println("Select an option: ");
            
            // Intentionally brittle: no input checking. Entering a string here will crash it!
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            if (choice == 3) {
                System.out.println("Shutting down inventory console engine. Goodbye!");
                break;
            }
            
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter price: ");
            double price = scanner.nextDouble();
            
            System.out.print("Enter current stock quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            if (choice == 1) {
                PerishableProduct pProd = new PerishableProduct();
                pProd.name = name;
                pProd.price = price;
                pProd.quantity = quantity;
                
                System.out.print("Enter expiry date (YYYY-MM-DD): ");
                String dateInput = scanner.nextLine();
                pProd.expiryDate = LocalDate.parse(dateInput);
                
                // Fire directly into the raw database layer
                productService.addProductToInventory(pProd);
                
            } else if (choice == 2) {
                DurableProduct dProd = new DurableProduct();
                dProd.name = name;
                dProd.price = price;
                dProd.quantity = quantity;
                
                System.out.print("Enter warranty duration (in months): ");
                int warranty = scanner.nextInt();
                dProd.warrantyMonths = warranty;
                
                // Fire directly into the raw database layer
                productService.addProductToInventory(dProd);
            }
        }
scanner.close();
 }
 
}
