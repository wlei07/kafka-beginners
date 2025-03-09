# saga-pattern-spring-boot-demo

Demonstration of SAGA Orchestration Design Pattern using Spring Boot and Kafka

Flow:

# orders-service
1. order service publish OrderCreatedEvent to the topic orders.events.topic.name 
2. OrderSaga picks up the OrderCreatedEvent above, publishes ReserveProductCommand to products.commands.topic.name
3. 