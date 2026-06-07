package com.stocksync.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
}

// package com.stocksync.inventory;

// import com.stocksync.inventory.model.Product;
// import com.stocksync.inventory.repository.ProductRepository;
// import com.stocksync.inventory.service.ProductService;
// import com.stocksync.inventory.exception.InvalidProductException;
// import com.stocksync.inventory.exception.InsufficientStockException;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import java.util.Scanner;

// @SpringBootApplication
// public class InventoryApplication implements CommandLineRunner {

//     @Autowired
//     private ProductService productService;

//     @Autowired
//     private ProductRepository productRepository;

//     public static void main(String[] args) {
//         SpringApplication app = new SpringApplication(InventoryApplication.class);
//         app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE); // 2. Forces headless console mode
//         app.run(args);
//     }

//     @Override
//     public void run(String... args) throws Exception { // 3. This method intercepts the boot sequence to display your menu
//         Scanner scanner = new Scanner(System.in);
//         System.out.println("=== Welcome to Stock-Sync JPA Console Engine ===");
        
//         while (true) {
//             System.out.println("\n--- INVENTORY CATEGORY OPERATIONS MENU ---");
//             System.out.println("1. Create New Product Profile");
//             System.out.println("2. View All Stock Records (Read - JPA)");
//             System.out.println("3. Add Quantity to Existing Stock (+)");
//             System.out.println("4. Subtract Quantity from Existing Stock (-)");
//             System.out.println("5. Explicitly Update Quantity Value");
//             System.out.println("6. Delete Product Profile (Delete - JPA)");
//             System.out.println("7. Exit Application");
//             System.out.print("Select an option: ");
            
//             if (!scanner.hasNextInt()) {
//                 break;
//             }
            
//             int choice = scanner.nextInt();
//             scanner.nextLine(); // Clear buffer
            
//             if (choice == 7) {
//                 System.out.println("Shutting down inventory console engine. Goodbye!");
//                 break;
//             }
            
//             try {
//                 switch(choice) {
//                     case 1:
//                         Product p = new Product();
//                         System.out.print("Enter product name: "); p.setName(scanner.nextLine());
//                         System.out.print("Enter category: "); p.setCategory(scanner.nextLine());
//                         System.out.print("Enter price: "); p.setPrice(scanner.nextDouble());
//                         System.out.print("Enter initial stock quantity: "); p.setQuantity(scanner.nextInt());
//                         scanner.nextLine(); 
//                         System.out.print("Enter short description: "); p.setDescription(scanner.nextLine());
//                         System.out.print("Enter supplier vendor name: "); p.setSupplier(scanner.nextLine());
                        
//                         productService.addProductToInventory(p);
//                         break;
                        
//                     case 2:
//                         System.out.println("\n=========================== CURRENT STOCK LEVEL INVENTORY ===========================");
//                         productRepository.findAll().forEach(item -> {
//                             System.out.println("ID: " + item.getId() + 
//                                                " | Name: " + item.getName() + 
//                                                " | Cat: " + item.getCategory() +
//                                                " | Price: $" + item.getPrice() + 
//                                                " | Qty: " + item.getQuantity() +
//                                                " | Supp: " + item.getSupplier() +
//                                                " | Desc: " + item.getDescription());
//                         });
//                         System.out.println("====================================================================================");
//                         break;
                        
//                     case 3:
//                         System.out.print("Enter Product ID: "); int addId = scanner.nextInt();
//                         System.out.print("Enter quantity to add (+): "); int addVol = scanner.nextInt();
//                         productService.adjustStockVolume(addId, addVol);
//                         break;
                        
//                     case 4:
//                         System.out.print("Enter Product ID: "); int subId = scanner.nextInt();
//                         System.out.print("Enter quantity to subtract (-): "); int subVol = scanner.nextInt();
//                         productService.adjustStockVolume(subId, -subVol);
//                         break;
                        
//                     case 5:
//                         System.out.print("Enter Product ID: "); int updateId = scanner.nextInt();
//                         System.out.print("Enter new total stock count: "); int explicitQty = scanner.nextInt();
                        
//                         Product existing = productRepository.findById(updateId)
//                                 .orElseThrow(() -> new InvalidProductException("Product profile ID does not exist."));
//                         existing.setQuantity(explicitQty);
//                         productRepository.save(existing);
//                         System.out.println("Quantity overwritten successfully!");
//                         break;
                        
//                     case 6:
//                         System.out.print("Enter Product ID to drop: "); int deleteId = scanner.nextInt();
//                         if (!productRepository.existsById(deleteId)) {
//                             throw new InvalidProductException("Product profile ID does not exist.");
//                         }
//                         productRepository.deleteById(deleteId);
//                         System.out.println("Product dropped completely from system.");
//                         break;
                        
//                     default:
//                         System.out.println("Invalid option selected.");
//                 }
//             } catch (InvalidProductException | InsufficientStockException e) {
//                 System.out.println("\n[APPLICATION NOTICE] -> " + e.getMessage());
//             } catch (Exception e) {
//                 System.out.println("\n[SYSTEM ERROR] An unexpected error occurred: " + e.getMessage());
//                 if(scanner.hasNextLine()) scanner.nextLine(); 
//             }
//             finally {
//                 System.out.println("\nReturning to main menu...");
            
//             }
//         }
//     }
// }
