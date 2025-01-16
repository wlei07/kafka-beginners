package com.example.products_microservice;

import java.util.Date;

public record ErrorMessage (Date timestamp, String message, String details) {
}
