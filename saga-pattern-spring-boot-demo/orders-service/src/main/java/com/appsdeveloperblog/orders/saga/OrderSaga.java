package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.dto.commands.ApproveOrderCommand;
import com.appsdeveloperblog.core.dto.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.core.dto.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.core.dto.commands.ProductReservationCanceledEvent;
import com.appsdeveloperblog.core.dto.commands.RejectOrderCommand;
import com.appsdeveloperblog.core.dto.commands.ReserveProductCommand;
import com.appsdeveloperblog.core.dto.events.OrderApprovedEvent;
import com.appsdeveloperblog.core.dto.events.OrderCreatedEvent;
import com.appsdeveloperblog.core.dto.events.PaymentProcessedEvent;
import com.appsdeveloperblog.core.dto.events.PaymentProcessingFailedEvent;
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
@KafkaListener(topics = {"${orders.events.topic.name}", "${products.events.topic.name}", "${payments.events.topic.name}"})
public class OrderSaga {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderHistoryService orderHistoryService;

    @Value("${products.commands.topic.name}")
    private String productsCommandsTopicName;
    @Value("${payments.commands.topic.name}")
    private String paymentsCommandsTopicName;
    @Value("${orders.commands.topic.name}")
    private String ordersCommandsTopicName;

    @KafkaHandler
    public void handleOrderCreatedEvent(@Payload OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = new ReserveProductCommand(
                orderCreatedEvent.productId(),
                orderCreatedEvent.productQuantity(),
                orderCreatedEvent.orderId()
        );
        kafkaTemplate.send(productsCommandsTopicName, reserveProductCommand);
        orderHistoryService.add(orderCreatedEvent.orderId(), OrderStatus.CREATED);
    }

    @KafkaHandler
    public void handleProductReservedEvent(@Payload ProductReservedEvent productReservedEvent) {
        ProcessPaymentCommand processPaymentCommand = new ProcessPaymentCommand(
                productReservedEvent.orderId(),
                productReservedEvent.productId(),
                productReservedEvent.productPrice(),
                productReservedEvent.productQuantity()
        );
        kafkaTemplate.send(paymentsCommandsTopicName, processPaymentCommand);
    }

    @KafkaHandler
    public void handleProductReservationCancelledEvent(@Payload ProductReservationCanceledEvent productReservationCanceledEvent) {
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCanceledEvent.orderId());
        kafkaTemplate.send(ordersCommandsTopicName, rejectOrderCommand);
        orderHistoryService.add(productReservationCanceledEvent.orderId(), OrderStatus.REJECTED);
    }

    @KafkaHandler
    public void handlePaymentProcessedEvent(@Payload PaymentProcessedEvent paymentProcessedEvent) {
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.orderId());
        kafkaTemplate.send(ordersCommandsTopicName, approveOrderCommand);
    }

    @KafkaHandler
    public void handlePaymentProcessingFailedEvent(@Payload PaymentProcessingFailedEvent paymentProcessingFailedEvent) {
        CancelProductReservationCommand cancelProductReservationCommand = new CancelProductReservationCommand(
                paymentProcessingFailedEvent.productId(),
                paymentProcessingFailedEvent.orderId(),
                paymentProcessingFailedEvent.productQuantity()
        );
        kafkaTemplate.send(productsCommandsTopicName, cancelProductReservationCommand);
    }

    @KafkaHandler
    public void handleOrderApprovedEvent(@Payload OrderApprovedEvent orderApprovedEvent) {
        orderHistoryService.add(orderApprovedEvent.orderId(), OrderStatus.APPROVED);
    }
}
