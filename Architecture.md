1. The Stock-Sync Expanded Layered Architecture
To keep the project clean, scalable, and modular, we establish distinct logical boundaries:  

A. Presentation Layer (Two Entry Points)

REST API Controller (Controller Layer): Exposes standard HTTP endpoints (like @RestController) enabling access via tools like Postman.  

Java Console UI: A localized command-line text loop running directly inside your terminal.

Rule: Both entry points must act merely as routing adapters. They collect data inputs and forward them strictly downward to the Service Layer.  

B. Business Logic Layer (Service Layer)

Core Components: Service implementation classes (e.g., ProductService).

Responsibility: The core decision engine of Stock-Sync. It runs standard computations, enforces constraints (such as preventing negative entry additions), and isolates operations.  

C. Data Access Object Layer (Repository Layer)

Core Components: Data interfaces and concrete objects interacting with storage (e.g., ProductRepository or ProductDAO).

Responsibility: Contains the actual structural commands sending queries down into your persistent database rows.  

D. Data Domain Layer (Model Layer)

Core Components: OOP structures (Abstract Product, PerishableProduct, DurableProduct).  

Responsibility: Defines data models holding state across all execution chains.  

