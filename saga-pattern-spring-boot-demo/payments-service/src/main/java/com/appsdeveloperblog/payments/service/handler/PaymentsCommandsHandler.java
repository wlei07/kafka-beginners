package com.appsdeveloperblog.payments.service.handler;

import com.appsdeveloperblog.core.dto.Payment;
import com.appsdeveloperblog.core.dto.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.core.dto.events.PaymentProcessedEvent;
import com.appsdeveloperblog.core.dto.events.PaymentProcessingFailedEvent;
import com.appsdeveloperblog.core.exceptions.CreditCardProcessorUnavailableException;
import com.appsdeveloperblog.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${payments.commands.topic.name}")
@RequiredArgsConstructor
@Slf4j
public class PaymentsCommandsHandler {
    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${payments.events.topic.name}")
    private String paymentEventsTopicName;

    @KafkaHandler
    void handleProcessPaymentCommand(ProcessPaymentCommand processPaymentCommand) {
        Payment payment = new Payment(
                processPaymentCommand.orderId(),
                processPaymentCommand.productId(),
                processPaymentCommand.productPrice(),
                processPaymentCommand.productQuantity()
        );
        try {
            Payment processedPayment = paymentService.process(payment);
            PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(processedPayment.getOrderId(), processedPayment.getId());
            kafkaTemplate.send(paymentEventsTopicName, paymentProcessedEvent);
        } catch (CreditCardProcessorUnavailableException e) {
            log.error(e.getMessage(), e);
            PaymentProcessingFailedEvent paymentProcessingFailedEvent = new PaymentProcessingFailedEvent(
                    processPaymentCommand.orderId(),
                    processPaymentCommand.productId(),
                    processPaymentCommand.productQuantity()
            );
            kafkaTemplate.send(paymentEventsTopicName, paymentProcessingFailedEvent);
        }
    }
}
