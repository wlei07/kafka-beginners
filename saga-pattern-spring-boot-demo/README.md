# saga-pattern-spring-boot-demo

Demonstration of SAGA Orchestration Design Pattern using Spring Boot and Kafka

Flow:

# orders-service
1. OrderServiceImpl: OrderCreatedEvent -> ${orders.events.topic.name}
2. OrderSaga: ${orders.events.topic.name} -> OrderCreatedEvent, ReserveProductCommand -> ${products.commands.topic.name}
# products-service
1. ProductCommandHandler: ${products.commands.topic.name} -> ReserveProductCommand
2. reserve product, and if successful
3. ProductReservedEvent -> ${products.events.topic.name}
4. if not successful
5. ProductReservationFailedEvent -> ${products.events.topic.name}
# orders-service
1. ${products.events.topic.name} -> ProductReservedEvent
2. ProcessPaymentCommand -> ${payments.commands.topic.name}
# payments-service
1. ${payments.commands.topic.name} -> ProcessPaymentCommand
2. PaymentProcessedEvent or PaymentProcessingFailedEvent -> ${payments.events.topic.name}
# orders-service
1. ${payments.events.topic.name} -> (OrderSaga) PaymentProcessedEvent
2. (OrderSage) ApproveOrderCommand -> ${orders.commands.topic.name}
3. (OrderCommandsHandler) ${orders.commands.topic.name} -> (OrderService#approveOrder) OrderApprovedEvent -> ${orders.events.topic.name}
