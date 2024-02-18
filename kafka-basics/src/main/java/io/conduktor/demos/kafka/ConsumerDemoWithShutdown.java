package io.conduktor.demos.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class ConsumerDemoWithShutdown {
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
            // get a reference to the main thread
            final Thread mainThread = Thread.currentThread();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                kafkaConsumer.wakeup();
                // join the main thread to allow the execution of the code in the main thread.
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    log.error("#######################");
                    throw new RuntimeException(e);
                }
            }));
            try {
                kafkaConsumer.subscribe(Collections.singletonList(topic));
                while (true) {
                    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
                    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                        log.info("Key: " + consumerRecord.key() + ", Value: " + consumerRecord.value());
                        log.info("Partition: " + consumerRecord.partition() + ", Offset: " + consumerRecord.offset());
                    }
                }
            } catch (WakeupException e) {
                log.error("Consumer is starting to shutdown.", e);
            } catch (Exception e) {
                log.error("Unexpected exception in the consumer", e);
            }
        }
    }
}
