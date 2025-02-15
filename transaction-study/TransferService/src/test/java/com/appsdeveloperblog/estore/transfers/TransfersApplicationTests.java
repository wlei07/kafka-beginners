package com.appsdeveloperblog.estore.transfers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(count = 3)
class TransfersApplicationTests {

	@Test
	void contextLoads() throws InterruptedException {
	}
}
