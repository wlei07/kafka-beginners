# saga-pattern-spring-boot-demo

Demonstration of SAGA Orchestration Design Pattern using Spring Boot and Kafka

Flow:

# orders-service
1. order service publish OrderCreatedEvent to the topic ${orders.events.topic.name} 
2. OrderSaga picks up the OrderCreatedEvent above, publishes ReserveProductCommand to ${products.commands.topic.name}
# products-service
1. picks up the ReserveProductCommand published in the above step from topic ${products.commands.topic.name}
2. reserve product, and if successful
3. publish ProductReservedEvent to the topic ${products.events.topic.name}
4. if not successful publish ProductReservationFailedEvent to the same topic as mentioned above
