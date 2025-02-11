package com.example.products_microservice;

import com.example.core.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Value("${app.topic-name}")
    private String topicName;
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Override
    public String createProduct(CreateProductRequest product) throws ExecutionException, InterruptedException {
        return createProductSynchronously(product);
    }

    private String createProductSynchronously(CreateProductRequest product) throws InterruptedException, ExecutionException {
        String productId = UUID.randomUUID().toString();
        // TODO: persist product into database before publishing an event.
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, product.title(), product.price(), product.quantity());
        log.info("Before publishing a {}", ProductCreatedEvent.class.getSimpleName());

        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<>(topicName, productId, productCreatedEvent);
        // to demonstrate how to use header when sending a message
        // note it is coupled with consumer side, so when you change it, you also need to update consumer.
        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send(record).get();
        log.info("Partition: {}", result.getRecordMetadata().partition());
        log.info("Topic: {}", result.getRecordMetadata().topic());
        log.info("Offset: {}", result.getRecordMetadata().offset());
        return productId;
    }

    private String createProductAsynchronously(CreateProductRequest product) {
        String productId = UUID.randomUUID().toString();
        // TODO: persist product into database before publishing an event.
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, product.title(), product.price(), product.quantity());
        // send message asynchronously:
        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send(topicName, productId, productCreatedEvent);
        future.whenComplete((r, e) -> {
            if (e != null) {
                log.error("failed to send product event", e);
            } else {
                log.info("product event sent successfully {}", r.getRecordMetadata());
            }
        });
        // to force a wait for the sending message returned, i.e., synchronous
        // future.join();
        log.info("returning product id {}", productId);
        return productId;
    }
}
