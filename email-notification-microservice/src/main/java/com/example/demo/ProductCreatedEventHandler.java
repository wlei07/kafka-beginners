package com.example.demo;

import com.example.core.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
// group id could be configured here:
// groupId = "email-notification-group"
@KafkaListener(topics = "product-created-events-topic")
@Slf4j
@RequiredArgsConstructor
public class ProductCreatedEventHandler {
    private final RestTemplate restTemplate;

    @KafkaHandler
    public void handle(
            @Payload ProductCreatedEvent productCreatedEvent,
            @Header(value = "messageId", required = true) String messageId,
            @Header(KafkaHeaders.RECEIVED_KEY) String messageKey
    ) {
        log.info("Received product created event: {} with productId: {}", productCreatedEvent.title(), productCreatedEvent.productId());
        // to simulate not retryable exception happened during message handling.
        // throw new NotRetryableException("An error took place. No need to consume this message again.");

        String requestUrl = "http://localhost:8082/response/200";
        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Received response from a remote service: {}", response.getBody());
            }
        } catch (ResourceAccessException e) {
            log.error(e.getMessage(), e);
            throw new RetryableException(e);
        } catch (HttpServerErrorException e) {
            log.error(e.getMessage(), e);
            throw new NotRetryableException(e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NotRetryableException(e);
        }
    }
}
