package io.leiwang.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import io.leiwang.kafka.common.KafkaPropertiesBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {
    public static void main(String[] args) throws InterruptedException {
        // create Producer properties
        Properties properties = new KafkaPropertiesBuilder()
                .bootStrapServersUpstash()
                //.bootStrapServersLocal()
                .keyValueStringSerializer()
                .build();
        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        String topic = "wikimedia.recentchange";
        BackgroundEventHandler backgroundEventHandler = new WikimediaChangeHandler(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder eventSourceBuilder = new EventSource.Builder(URI.create(url));
        BackgroundEventSource.Builder backGroundEventSourceBuilder = new BackgroundEventSource.Builder(backgroundEventHandler, eventSourceBuilder);
        try (BackgroundEventSource backgroundEventSource = backGroundEventSourceBuilder.build()) {
            backgroundEventSource.start();
        }
        TimeUnit.SECONDS.sleep(5);
    }
}
