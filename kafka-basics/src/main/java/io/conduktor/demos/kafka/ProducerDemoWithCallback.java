package io.conduktor.demos.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

@Slf4j
public class ProducerDemoWithCallback {
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
        for(int j=0;j<10;j++) {
            for (int i = 0; i < 30; i++) {
                // create a producer record
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "hello world " + i);

                // send data
                kafkaProducer.send(producerRecord, new LogMetaCallBack());
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
