package com.appsdeveloperblog.products.service.handler;

import com.appsdeveloperblog.core.dto.Product;
import com.appsdeveloperblog.core.dto.commands.ReserveProductCommand;
import com.appsdeveloperblog.core.dto.events.ProductReservedEvent;
import com.appsdeveloperblog.core.exceptions.ProductInsufficientQuantityException;
import com.appsdeveloperblog.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${products.commands.topic.name}")
@RequiredArgsConstructor
@Slf4j
public class ProductCommandHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductService productService;

    @Value("${products.events.topic.name}")
    private String productEventsTopicName;

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand command) {
        try {
            Product desiredProduct = new Product(command.productId(), command.productQuantity());
            Product reservedProduct = productService.reserve(desiredProduct, command.orderId());
            ProductReservedEvent productReservedEvent = new ProductReservedEvent(
                    command.orderId(),
                    command.productId(),
                    reservedProduct.getPrice(),
                    command.productQuantity()
            );
            kafkaTemplate.send(productEventsTopicName, productReservedEvent);
        } catch (ProductInsufficientQuantityException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
