package io.conduktor.demos.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

@Slf4j
public class ProducerDemoKeys {
    public static void main(String[] args) {
        log.info("I am a Kafka producer!");

        // create Producer Properties
        Properties properties = new KafkaPropertiesBuilder()
                .bootStrapServersUpstash()
                .keyValueStringSerializer()
                .build();

        // create the Producer
        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties)) {
            sendMessages(kafkaProducer);
            // flush and close the Producer
            kafkaProducer.flush();
        }
    }

    private static void sendMessages(KafkaProducer<String, String> kafkaProducer) {
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                String topic = "demo_java";
                String key = "id_" + i;
                String value = "hello world " + i;
                // create a producer record
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

                // send data
                kafkaProducer.send(producerRecord, ((metadata, exception) -> onCompletion(key, metadata, exception)));
            }
            kafkaProducer.flush();
        }
    }

    private static void onCompletion(String key, RecordMetadata metadata, Exception exception) {
        // executes every time a record successfully sent or on exception is thrown
        if (exception == null) {
            // the record was successfully sent
            log.info("Key: " + key + " | Partition: " + metadata.partition());
        } else {
            log.error("Error while producing", exception);
        }
    }
}
