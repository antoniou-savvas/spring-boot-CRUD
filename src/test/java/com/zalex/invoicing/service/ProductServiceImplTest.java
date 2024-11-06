package com.zalex.invoicing.service;

import com.zalex.invoicing.exception.ResourceNotFoundException;
import com.zalex.invoicing.model.Product;
import com.zalex.invoicing.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetProduct() {
        Product product = new Product();
        product.setProductId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProduct(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getProductId());
    }

    @Test
    public void testGetProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(1L));
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product();
        product.setProductId(1L);
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(product);

        assertNotNull(updatedProduct);
        assertEquals(1L, updatedProduct.getProductId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testDeleteProduct() {
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
    }
}
