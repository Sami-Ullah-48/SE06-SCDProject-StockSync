package com.stocksync.inventory.Model;
import java.time.LocalDate;

public class PerishableProduct extends Product {
    public LocalDate expiryDate;

    @Override
    public double calculateStockValue() {
        // Simple raw calculation with no safety boundaries or risk factor controls
        return this.price * this.quantity;
    }
}
