package com.appsdeveloperblog.products.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static com.appsdeveloperblog.core.CreateTopicUtil.createTopic;

@Configuration
public class KafkaConfig {
    @Value("${products.events.topic.name}")
    private String productEventsTopicName;

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    NewTopic createProductEventsTopic() {
        return createTopic(productEventsTopicName);
    }
}
