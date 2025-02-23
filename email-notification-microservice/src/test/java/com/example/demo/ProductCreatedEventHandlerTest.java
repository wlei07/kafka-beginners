package com.example.demo;

import com.example.core.ProductCreatedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}")
class ProductCreatedEventHandlerTest {
    @MockitoBean
    private ProcessedEventRepository processedEventRepository;
    @MockitoBean
    private RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @MockitoSpyBean
    private ProductCreatedEventHandler productCreatedEventHandler;

    @Test
    void handle() throws ExecutionException, InterruptedException {
        // Arrange
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(UUID.randomUUID().toString(), "Test product", new BigDecimal(100), 1);
        String messageId = UUID.randomUUID().toString();
        String messageKey = productCreatedEvent.productId();
        ProducerRecord<String, Object> record = new ProducerRecord<>("product-created-events-topic", messageKey, productCreatedEvent);
        record.headers().add("messageId", messageId.getBytes());
        record.headers().add(KafkaHeaders.RECEIVED_KEY, messageKey.getBytes());

        ProcessedEventEntity processedEventEntity = new ProcessedEventEntity();
        when(processedEventRepository.findByMessageId(anyString())).thenReturn(processedEventEntity);
        when(processedEventRepository.save(any(ProcessedEventEntity.class))).thenReturn(null);
        String responseBody = "{\"key\": \"value\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), isNull(), eq(String.class))).thenReturn(responseEntity);

        // Act
        kafkaTemplate.send(record).get();

        // Assert
        verify(productCreatedEventHandler, timeout(5000).times(1)).handle(
                argThat(productCreatedEventArg -> {
                    assertThat(productCreatedEventArg).isNotNull();
                    assertThat(productCreatedEventArg.productId()).isEqualTo(messageKey);
                    assertThat(productCreatedEventArg.title()).isEqualTo("Test product");
                    assertThat(productCreatedEventArg.price()).isEqualTo(new BigDecimal(100));
                    assertThat(productCreatedEventArg.quantity()).isEqualTo(1);
                    return true;
                }),
                argThat(messageIdArg -> {
                    assertThat(messageIdArg).isEqualTo(messageId);
                    return true;
                }),
                argThat(messageKeyArg -> {
                    assertThat(messageKeyArg).isEqualTo(messageKey);
                    return true;
                }));
    }
}
