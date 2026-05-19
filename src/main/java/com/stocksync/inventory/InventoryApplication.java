package com.stocksync.inventory;

import com.stocksync.inventory.Model.*;
import com.stocksync.inventory.Service.ProductService;
import com.stocksync.inventory.exception.InsufficientStockException;
import com.stocksync.inventory.exception.InvalidProductException;
import com.stocksync.inventory.Repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.*;

@SpringBootApplication
public class InventoryApplication implements CommandLineRunner {
@Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

	public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

 @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Welcome to Refactored Spring Data JPA Stock Engine ===");
        
        while (true) {
            System.out.println("\n--- INVENTORY SYSTEM OPERATIONAL MENU ---");
            System.out.println("1. Create New Product Profile");
            System.out.println("2. View All Stock Records (JPA)");
            System.out.println("3. Add Quantity to Existing Stock (+)");
            System.out.println("4. Subtract Quantity from Existing Stock (-)");
            System.out.println("5. Explicitly Update Quantity Value");
            System.out.println("6. Delete Product Profile (JPA)");
            System.out.println("7. Exit Application");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            if (choice == 7) {
                System.out.println("Shutting down inventory console engine. Goodbye!");
                break;
            }
            
            try {
                switch(choice) {
                    case 1:
                        Product p = new Product();
                        System.out.print("Enter product name: "); p.setName(scanner.nextLine());
                        System.out.print("Enter category: "); p.setCategory(scanner.nextLine());
                        System.out.print("Enter price: "); p.setPrice(scanner.nextDouble());
                        System.out.print("Enter initial stock quantity: "); p.setQuantity(scanner.nextInt());
                        scanner.nextLine();
                        System.out.print("Enter short description: "); p.setDescription(scanner.nextLine());
                        System.out.print("Enter supplier vendor name: "); p.setSupplier(scanner.nextLine());
                        
                        productService.addProductToInventory(p);
                        break;
                        
                    case 2:
                        // Inlined view reading using native Spring Data JPA built-in lists
                        System.out.println("\n=========================== CURRENT STOCK LEVEL INVENTORY ===========================");
                        productRepository.findAll().forEach(item -> {
                            System.out.println("ID: " + item.getId() + 
                                               " | Name: " + item.getName() + 
                                               " | Cat: " + item.getCategory() +
                                               " | Price: $" + item.getPrice() + 
                                               " | Qty: " + item.getQuantity() +
                                               " | Supp: " + item.getSupplier() +
                                               " | Desc: " + item.getDescription());
                        });
                        System.out.println("====================================================================================");
                        break;
                        
                    case 3:
                        System.out.print("Enter Product ID: "); int addId = scanner.nextInt();
                        System.out.print("Enter quantity to add (+): "); int addVol = scanner.nextInt();
                        productService.adjustStockVolume(addId, addVol);
                        break;
                        
                    case 4:
                        System.out.print("Enter Product ID: "); int subId = scanner.nextInt();
                        System.out.print("Enter quantity to subtract (-): "); int subVol = scanner.nextInt();
                        productService.adjustStockVolume(subId, -subVol);
                        break;
                        
                    case 5:
                        System.out.print("Enter Product ID: "); int updateId = scanner.nextInt();
                        System.out.print("Enter new total stock count: "); int explicitQty = scanner.nextInt();
                        
                        Product existing = productRepository.findById(updateId)
                                .orElseThrow(() -> new InvalidProductException("Product not found."));
                        existing.setQuantity(explicitQty);
                        productRepository.save(existing);
                        System.out.println("Product ID " + updateId + " quantity explicitly overwritten successfully!");
                        break;
                        
                    case 6:
                        System.out.print("Enter Product ID to drop: "); int deleteId = scanner.nextInt();
                        if (!productRepository.existsById(deleteId)) {
                            throw new InvalidProductException("Product profile ID does not exist.");
                        }
                        productRepository.deleteById(deleteId); // Native Spring Data JPA automated removal execution
                        System.out.println("Product ID " + deleteId + " wiped out completely from database tables.");
                        break;
                        
                    default:
                        System.out.println("Invalid option selected.");
                }
            } catch (InvalidProductException | InsufficientStockException e) {
                System.out.println("\n[APPLICATION NOTICE] -> " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n[SYSTEM ERROR] Unexpected failure: " + e.getMessage());
                scanner.nextLine();
            }
            finally{
                System.out.println("\nPress Enter to continue...");
                scanner.close();
            }
        }
    }
}
