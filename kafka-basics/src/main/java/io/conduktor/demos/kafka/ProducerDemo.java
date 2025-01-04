package io.conduktor.demos.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import io.leiwang.kafka.common.KafkaPropertiesBuilder;

import java.util.Properties;

@Slf4j
public class ProducerDemo {
    public static void main(String[] args) {
        log.info("I am a Kafka producer!");

        // create Producer Properties
        Properties properties = new KafkaPropertiesBuilder()
                .bootStrapServersLocal()
                .keyValueStringSerializer()
                .build();

        // create the Producer
        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties)) {
            // create a producer record
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "hello world 2");

            // send data
            kafkaProducer.send(producerRecord);

            // flush and close the Producer
            kafkaProducer.flush();
        }
    }
}
