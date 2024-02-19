package io.leiwang.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import io.leiwang.kafka.common.KafkaPropertiesBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class WikimediaChangesProducer {
    public static void main(String[] args) {
        // create Producer properties
        Properties properties = new KafkaPropertiesBuilder()
                .bootStrapServersLocal()
                .keyValueStringSerializer()
                .build();
        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        String topic = "wikimedia.recentchange";
        // TODO EventHandler
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder eventSourceBuilder = new EventSource.Builder();
        EventSource eventSource = eventSourceBuilder.build();
        eventSource.start();
    }
}
