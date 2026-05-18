package com.stocksync.inventory.Model;

    
    public abstract class Product {
    public int id;
    public String name;
    public double price;
    public int quantity;

    public abstract double calculateStockValue();
}
