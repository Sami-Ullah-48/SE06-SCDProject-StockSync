package com.stocksync.inventory;

import com.stocksync.inventory.Model.*;
import com.stocksync.inventory.Service.ProductService;
import com.stocksync.inventory.exception.InsufficientStockException;
import com.stocksync.inventory.exception.InvalidProductException;
import com.stocksync.inventory.Repository.ProductDAO;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.*;

@SpringBootApplication
public class InventoryApplication implements CommandLineRunner {

	public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

 @Override
    public void run(String... args) throws Exception {
        ProductService productService = new ProductService();
        ProductDAO productDAO = new ProductDAO();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Welcome to Stock-Sync Engine ===");
        
        while (true) {
            System.out.println("\n--- INVENTORY CATEGORY OPERATIONS MENU ---");
            System.out.println("1. Create New Product Profile");
            System.out.println("2. View All Stock Records (Read)");
            System.out.println("3. Add Quantity to Existing Stock (+)");
            System.out.println("4. Subtract Quantity from Existing Stock (-)");
            System.out.println("5. Explicitly Update Quantity Value");
            System.out.println("6. Delete Product Profile (Delete)");
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
                        System.out.print("Enter category (Grocery, Stationary, Cutlery, etc): "); p.setCategory(scanner.nextLine());
                    System.out.print("Enter price: "); p.setPrice(scanner.nextDouble());
                    System.out.print("Enter initial stock quantity: "); p.setQuantity(scanner.nextInt());
                    scanner.nextLine(); // Clear buffer
                    System.out.print("Enter product short description: "); p.setDescription(scanner.nextLine());
                    System.out.print("Enter supplier vendor name: "); p.setSupplier(scanner.nextLine());
                    
                    productService.addProductToInventory(p);
                    break;
                    
                case 2:
                    productDAO.printAllProducts();
                    break;
                    
                case 3:
                    System.out.print("Enter Product ID to add volume to: "); int addId = scanner.nextInt();
                    System.out.print("Enter quantity to add (+): "); int addVol = scanner.nextInt();
                    productService.adjustStockVolume(addId, addVol);
                    break;
                    
                case 4:
                    System.out.print("Enter Product ID to subtract volume from: "); int subId = scanner.nextInt();
                    System.out.print("Enter quantity to subtract (-): "); int subVol = scanner.nextInt();
                    productService.adjustStockVolume(subId, -subVol);
                    break;
                    
                case 5:
                    System.out.print("Enter Product ID to explicitly overwrite: "); int updateId = scanner.nextInt();
                    System.out.print("Enter new total stock count value: "); int explicitQty = scanner.nextInt();
                    productDAO.updateProductQuantity(updateId, explicitQty);
                    break;
                    
                case 6:
                    System.out.print("Enter Product ID to drop from database: "); int deleteId = scanner.nextInt();
                    productDAO.deleteProduct(deleteId);
                    break;
                    
                default:
                    System.out.println("Invalid option index selected.");
            }
        }catch(InvalidProductException | InsufficientStockException e){
            System.out.println("\\n[APPLICATION NOTICE] ->" + e.getMessage()); 
        }catch (Exception e) {
                // Catch-all safety guardrail for unexpected inputs (like typing letters into a number prompt)
                System.out.println("\n[SYSTEM ERROR] An unexpected system failure occurred: " + e.getMessage());
                scanner.nextLine(); // Reset buffer
            }
        finally{
            System.out.println("\nReturning to main menu...");
            scanner.close();
        }
    } 
}
}
