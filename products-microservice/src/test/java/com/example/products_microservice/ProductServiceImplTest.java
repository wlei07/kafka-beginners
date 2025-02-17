package com.example.products_microservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

class ProductServiceImplTest extends AbstractKafkaTest {
    Autowired
    private ProductService productService;

    @Test
    void createProduct() throws ExecutionException, InterruptedException {
        // Arrange
        String title = "iPhone 11";
        BigDecimal price = new BigDecimal(600);
        Integer quantity = 1;
        CreateProductRequest createProductRequest = new CreateProductRequest(title, price, quantity);

        // Act
        productService.createProduct(createProductRequest);

        // Assert
    }
}
