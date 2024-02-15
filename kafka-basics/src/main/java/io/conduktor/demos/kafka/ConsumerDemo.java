package io.conduktor.demos.kafka;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ConsumerDemo {
    public static void main(String[] args) {
        log.info("I am a Kafka consumer!");

        String groupId = "my-java-application";

        // create Producer Properties
        Properties properties = new KafkaPropertiesBuilder()
                .bootStrapServersLocal()
                .keyValueStringDeserializer()
                .groupId(groupId)
                .build();

    }
}
