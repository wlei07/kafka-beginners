package com.example.demo;

public class RetryableException extends RuntimeException {
    public RetryableException(String message) {}
    public RetryableException(Throwable cause) {}
}
