package com.example.CreateOrder.repositories;

import com.example.CreateOrder.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends JpaRepository<Product, String> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByNameOrDescription(String name, String description, Pageable pageable);
}
