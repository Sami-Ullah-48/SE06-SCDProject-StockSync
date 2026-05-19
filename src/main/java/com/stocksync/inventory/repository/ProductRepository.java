package com.stocksync.inventory.Repository;

import org.springframework.stereotype.Repository;
import com.stocksync.inventory.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
}
