package io.conduktor.demos.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;

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
                .bootStrapServersLocal()
                .keyValueStringDeserializer()
                .groupId(groupId)
                .autoOffsetReset(KafkaPropertiesBuilder.AutoOffsetReset.EARLIEST)
                .build();

        // create a consumer
        try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
            kafkaConsumer.subscribe(Collections.singletonList(topic));
        }
    }
}
