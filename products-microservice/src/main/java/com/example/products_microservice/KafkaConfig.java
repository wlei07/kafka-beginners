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
                .config("min.insync.replicas", "2")
                .build();
    }
}
