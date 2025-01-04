package io.conduktor.demos.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class LogMetaCallBack implements Callback {
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        // executes every time a record successfully sent or on exception is thrown
        if (exception == null) {
            // the record was successfully sent
            log.info("Received new metadata");
            log.info("Topic: " + metadata.topic());
            log.info("Partition: " + metadata.partition());
            log.info("Offset: " + metadata.offset());
            log.info("Timestamp: " + metadata.timestamp());
        } else {
            log.error("Error while producing", exception);
        }
    }
}
