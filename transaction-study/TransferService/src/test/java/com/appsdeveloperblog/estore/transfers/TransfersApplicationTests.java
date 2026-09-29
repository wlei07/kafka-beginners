package com.appsdeveloperblog.estore.transfers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
// count = 1 instead of 3: with Kafka 4.2.x (Spring Boot 4.1.1), a multi-node embedded KRaft cluster crashes at startup
// ("Received unexpected invalid request error" in KafkaRaftClient.handleUpdateVoterResponse) and the test hangs.
// Kafka 4.3 is not yet supported by spring-kafka-test 4.1.1.
// Closest upstream report: https://issues.apache.org/jira/browse/KAFKA-19867
// TODO: switch back to count = 3 (and topic-replicas in src/test/resources/application.yaml to 3) once this is fixed.
@EmbeddedKafka(count = 1)
class TransfersApplicationTests {

	@Test
	void contextLoads() throws InterruptedException {
	}
}
