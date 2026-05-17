1. System Requirements & Scope
OOP Entity Models and Inheritance Structure
To fulfill your requirement for utilizing OOP inheritance, we will model a base abstract class along with distinct product sub-types:

Product: (Abstract base Entity) Represents an item in the inventory.

id (Unique Identifier/Primary Key)

name (String)

price (Double)

quantity (Integer)

Abstract Method : calculateStockValue (polymorphism)
PerishableProduct (Extends Product)   
expiryDate: LocalDate
Override Method: calculateStockValue() (e.g., applies a discount if the item is close to expiring) 

DurableProduct (Extends Product) 
warrantyPeriodMonths: int

Override Method: calculateStockValue() (Standard asset value calculation) 

InventoryLog: Tracks stock changes (for when we want to audit what happened).

logId (Unique Identifier)

productId (Foreign Key)

changeQuantity (Integer, e.g., +10 for restock, -2 for sale)

timestamp (DateTime)

Core CRUD Operations (Console Interface)
The Java console application will present a text-based menu to the user with the following functionalities:

Create: Add a brand new product to the system.

Read:

View all products in the inventory.

Search for a specific product by its ID.

Update: Update stock levels (Restock / Dispense).

Delete: Remove a product from the tracking system entirely.