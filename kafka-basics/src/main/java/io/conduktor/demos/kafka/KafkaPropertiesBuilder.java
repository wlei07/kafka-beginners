package io.conduktor.demos.kafka;

import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaPropertiesBuilder {
    private static final String BOOTSTRAP_SERVERS_KEY = "bootstrap.servers";
    private final Properties properties;
    public KafkaPropertiesBuilder() {
        properties = new Properties();
    }

    public KafkaPropertiesBuilder bootStrapServersLocal() {
        properties.put(BOOTSTRAP_SERVERS_KEY, "localhost:9092");
        return this;
    }

    public KafkaPropertiesBuilder bootStrapServersUpstash() {
        properties.put("bootstrap.servers", "https://logical-cat-7023-eu2-kafka.upstash.io:9092");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"bG9naWNhbC1jYXQtNzAyMySgZ_fvxBMUUMmLMhNybPGsZCGsCYIerpujqAStSYc\" password=\"YzZhMWIyYTMtZmQyNC00M2VjLWI5MjQtOGIxYzZjYTRhYjcx\";");
        return this;
    }

    public KafkaPropertiesBuilder keyValueStringSerializer() {
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer .class.getName());
        return this;
    }

    public Properties build() {
        if(!properties.contains(BOOTSTRAP_SERVERS_KEY)) {
            throw new IllegalStateException("Please initialize property %s".formatted(BOOTSTRAP_SERVERS_KEY));
        }
        return properties;
    }
}
