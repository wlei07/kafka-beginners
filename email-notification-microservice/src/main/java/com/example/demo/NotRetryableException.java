package com.example.demo;

public class NotRetryableException extends RuntimeException {
    public NotRetryableException(String message) {}
    public NotRetryableException(Throwable cause) {}
}
