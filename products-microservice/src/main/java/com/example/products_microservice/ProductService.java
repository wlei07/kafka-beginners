package com.example.products_microservice;

import java.util.concurrent.ExecutionException;

public interface ProductService {
    String createProduct(CreateProductRequest product) throws ExecutionException, InterruptedException;
}
