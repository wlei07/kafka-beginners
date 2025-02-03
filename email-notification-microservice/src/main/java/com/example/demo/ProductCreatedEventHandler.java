package com.example.demo;

import com.example.core.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "product-created-events-topic")
@Slf4j
public class ProductCreatedEventHandler {
    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        log.info("Received product created event: {}", productCreatedEvent.title());
    }
}
