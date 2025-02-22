package com.example.products_microservice;

import com.example.core.ProductCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IdempotentProducerIntegrationTest {
    @Autowired
    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    @MockitoBean
    KafkaAdmin kafkaAdmin;

    @Test
    void testProducerConfig_whenIdempotenceEnabled_assertsIdempotentProperties() {
        // Arrange
        ProducerFactory<String, ProductCreatedEvent> producerFactory = kafkaTemplate.getProducerFactory();
        // Act
        Map<String, Object> config = producerFactory.getConfigurationProperties();
        // Assert
        assertThat(config.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG)).isEqualTo(Boolean.TRUE);
        assertThat(config.get(ProducerConfig.ACKS_CONFIG)).isEqualTo("all");

        if (config.containsKey(ProducerConfig.RETRIES_CONFIG)) {
            assertThat(Integer.parseInt(config.get(ProducerConfig.RETRIES_CONFIG).toString())).isGreaterThanOrEqualTo(1);
        }
    }
}
