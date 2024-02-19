package io.leiwang.kafka.common;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaPropertiesBuilder {
    private final Properties properties;

    public KafkaPropertiesBuilder() {
        properties = new Properties();
        // set small batch size to see the StickyPartitioner switch partition.
        // properties.put("batch.size", "400");
        // properties.put("partitioner.class", RoundRobinPartitioner.class.getName());
    }

    public KafkaPropertiesBuilder bootStrapServersLocal() {
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return this;
    }

    public KafkaPropertiesBuilder bootStrapServersUpstash() {
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "https://logical-cat-7023-eu2-kafka.upstash.io:9092");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"bG9naWNhbC1jYXQtNzAyMySgZ_fvxBMUUMmLMhNybPGsZCGsCYIerpujqAStSYc\" password=\"YzZhMWIyYTMtZmQyNC00M2VjLWI5MjQtOGIxYzZjYTRhYjcx\";");
        return this;
    }

    public KafkaPropertiesBuilder keyValueStringSerializer() {
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return this;
    }

    public KafkaPropertiesBuilder keyValueStringDeserializer() {
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        return this;
    }

    public KafkaPropertiesBuilder groupId(String groupId) {
        properties.put("group.id", groupId);
        return this;
    }

    @RequiredArgsConstructor
    public enum AutoOffsetReset {
        NONE("none"),
        EARLIEST("earliest"),
        LATEST("latest");
        private final String value;
    }

    public KafkaPropertiesBuilder autoOffsetReset(AutoOffsetReset autoOffsetReset) {
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset.value);
        return this;
    }

    public Properties build() {
        verifyBootstrapServersPropertyEntry();
        return properties;
    }

    private void verifyBootstrapServersPropertyEntry() {
        if (!properties.containsKey(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG)) {
            throw new IllegalStateException("Please initialize property %s".formatted(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        }
    }
}
