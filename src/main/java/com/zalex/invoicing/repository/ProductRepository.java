package com.zalex.invoicing.repository;

import com.zalex.invoicing.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
