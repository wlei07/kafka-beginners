package com.example.products_microservice;

import java.math.BigDecimal;

public record CreateProductRequest(String title, BigDecimal price, Integer quantity) {
}
