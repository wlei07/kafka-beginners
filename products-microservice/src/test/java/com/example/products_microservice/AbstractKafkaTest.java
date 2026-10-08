package com.example.products_microservice;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

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
    // Apicurio has no in-memory "mock://" registry like Confluent's, so the tests start a real one in Docker.
    // Started once for all test classes; Testcontainers removes it when the JVM exits.
    // Same version as the apicurio-registry image in infrastructure/compose.yaml.
    private static final int APICURIO_PORT = 8080;
    private static final GenericContainer<?> APICURIO_REGISTRY =
            new GenericContainer<>(DockerImageName.parse("apicurio/apicurio-registry:3.3.3"))
                    .withExposedPorts(APICURIO_PORT)
                    .waitingFor(Wait.forHttp("/health/ready").forPort(APICURIO_PORT));

    static {
        APICURIO_REGISTRY.start();
    }

    @DynamicPropertySource
    static void apicurioRegistryUrl(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.producer.properties.apicurio.registry.url",
                () -> "http://" + APICURIO_REGISTRY.getHost() + ":" + APICURIO_REGISTRY.getMappedPort(APICURIO_PORT)
                        + "/apis/registry/v3");
    }
}
