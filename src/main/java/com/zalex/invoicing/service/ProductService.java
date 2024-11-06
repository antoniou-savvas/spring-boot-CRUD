package com.zalex.invoicing.service;

import com.zalex.invoicing.model.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProduct(Long productId);
    Product updateProduct(Product product);
    void deleteProduct(Long productId);
    List<Product> getAllProducts();
}
