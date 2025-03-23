package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.core.dto.commands.ReserveProductCommand;
import com.appsdeveloperblog.core.dto.events.OrderCreatedEvent;
import com.appsdeveloperblog.core.dto.events.ProductReservedEvent;
import com.appsdeveloperblog.core.types.OrderStatus;
import com.appsdeveloperblog.orders.service.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderSaga {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderHistoryService orderHistoryService;

    @Value("${products.commands.topic.name}")
    private String productsCommandsTopicName;
    @Value("${payments.commands.topic.name}")
    private String paymentsCommandsTopicName;

    @KafkaListener(topics = {"${orders.events.topic.name}"})
    public void handleOrderCreatedEvent(@Payload OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = new ReserveProductCommand(
                orderCreatedEvent.productId(),
                orderCreatedEvent.productQuantity(),
                orderCreatedEvent.orderId()
        );
        kafkaTemplate.send(productsCommandsTopicName, reserveProductCommand);
        orderHistoryService.add(orderCreatedEvent.orderId(), OrderStatus.CREATED);
    }

    @KafkaListener(topics = "${products.events.topic.name}")
    public void handleProductReservedEvent(@Payload ProductReservedEvent productReservedEvent) {
        ProcessPaymentCommand processPaymentCommand = new ProcessPaymentCommand(
                productReservedEvent.orderId(),
                productReservedEvent.productId(),
                productReservedEvent.productPrice(),
                productReservedEvent.productQuantity()
        );
        kafkaTemplate.send(paymentsCommandsTopicName, processPaymentCommand);
    }
}
