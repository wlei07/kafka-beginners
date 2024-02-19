package io.conduktor.demos.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import io.leiwang.kafka.common.KafkaPropertiesBuilder;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class ConsumerDemo {
    public static void main(String[] args) {
        log.info("I am a Kafka consumer!");
        String groupId = "my-java-application";
        String topic = "demo_java";

        // create Producer Properties
        Properties properties = new KafkaPropertiesBuilder()
                .bootStrapServersUpstash()
                .keyValueStringDeserializer()
                .groupId(groupId)
                .autoOffsetReset(KafkaPropertiesBuilder.AutoOffsetReset.EARLIEST)
                .build();

        // create a consumer
        try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            while (true) {
                log.info("Polling.");
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    log.info("Key: " + consumerRecord.key() + ", Value: " + consumerRecord.value());
                    log.info("Partition: " + consumerRecord.partition() + ", Offset: " + consumerRecord.offset());
                }
            }
        }
    }
}
