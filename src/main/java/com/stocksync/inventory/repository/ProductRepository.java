package com.stocksync.inventory.repository;

import org.springframework.stereotype.Repository;

import com.stocksync.inventory.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByName(String name);
}
