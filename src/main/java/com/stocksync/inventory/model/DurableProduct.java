package com.stocksync.inventory.Model;

public class DurableProduct extends Product {
    public int warrantyMonths;

    @Override
    public double calculateStockValue() {
        return this.price * this.quantity;
    }
}
