package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.commands.ReserveProductCommand;
import com.appsdeveloperblog.core.dto.events.OrderCreatedEvent;
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
@KafkaListener(topics = "${orders.events.topic.name}")
@RequiredArgsConstructor
public class OrderSaga {
    private final KafkaTemplate<String, ReserveProductCommand> kafkaTemplate;
    private final OrderHistoryService orderHistoryService;

    @Value("${products.commands.topic.name}")
    private String productsCommandsTopicName;

    @KafkaHandler
    public void handleOrderCreatedEvent(@Payload OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand command = new ReserveProductCommand(
                orderCreatedEvent.productId(),
                orderCreatedEvent.productQuantity(),
                orderCreatedEvent.orderId()
        );
        kafkaTemplate.send(productsCommandsTopicName, command);
        orderHistoryService.add(orderCreatedEvent.orderId(), OrderStatus.CREATED);
    }
}
