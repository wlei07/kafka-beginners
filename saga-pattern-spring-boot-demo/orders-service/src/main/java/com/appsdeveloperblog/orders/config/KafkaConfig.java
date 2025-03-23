package com.appsdeveloperblog.orders.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static com.appsdeveloperblog.core.CreateTopicUtil.createTopic;

@Configuration
public class KafkaConfig {
    @Value("${orders.events.topic.name}")
    private String ordersEventsTopicName;
    @Value("${products.commands.topic.name}")
    private String productsCommandsTopicName;
    @Value("${payments.commands.topic.name}")
    private String paymentsCommandsTopicName;
    @Value("${orders.commands.topic.name}")
    private String ordersCommandsTopicName;

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    NewTopic createOrdersCommandsTopic() {
        return createTopic(ordersCommandsTopicName);
    }

    @Bean
    NewTopic createOrdersEventsTopic() {
        return createTopic(ordersEventsTopicName);
    }

    @Bean
    NewTopic createProductsCommandsTopic() {
        return createTopic(productsCommandsTopicName);
    }

    @Bean
    NewTopic createPaymentsCommandsTopic() {
        return createTopic(paymentsCommandsTopicName);
    }
}
