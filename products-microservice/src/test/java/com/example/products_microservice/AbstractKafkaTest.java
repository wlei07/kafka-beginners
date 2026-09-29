package com.example.products_microservice;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
// count = 1 instead of 3: with Kafka 4.2.x (Spring Boot 4.1.1), a multi-node embedded KRaft cluster crashes at startup
// ("Received unexpected invalid request error" in KafkaRaftClient.handleUpdateVoterResponse), and the idempotent
// producer then hangs forever waiting for a producer id. Kafka 4.3 is not yet supported by spring-kafka-test 4.1.1.
// Closest upstream report: https://issues.apache.org/jira/browse/KAFKA-19867
// TODO: switch back to count = 3 (and the topic settings in application-test.yaml to 3/2) once this is fixed.
@EmbeddedKafka(count = 1, partitions = 3)
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class AbstractKafkaTest {
}
