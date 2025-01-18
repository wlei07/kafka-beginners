package com.example.products_microservice;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${app.topic-name}")
    private String topicName;

    @Bean
    NewTopic createTopic() {
        return TopicBuilder
                .name(topicName)
                .partitions(3)
                .replicas(3)
                // min.insync.replicas for number of producers that must response for acknowledgement

                // spring.kafka.producer.reties=10, default is 2147483647
                // spring.kafka.producer.properties.retry.backoff.ms=1000, default is 100ms, time interval between reties.
                // AND alternatively:
                // spring.kafka.producer.properties.delivery.timeout.ms=120000, the maximum time producer can spend re-trying to deliver the message.
                // NOTE: delivery.timeout.ms >= linger.ms + request.timeout.ms
                // spring.kafka.producer.properties.linger.ms: wait and batch, then send all batched messages in one time.
                // spring.kafka.producer.properties.request.timeout.ms=30000, wait for a response after sending a request
                .config("min.insync.replicas", "2")
                .build();
    }
}
