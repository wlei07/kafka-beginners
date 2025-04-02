# saga-pattern-spring-boot-demo

Demonstration of SAGA Orchestration Design Pattern using Spring Boot and Kafka

Flow:

In high level, reserve product -> process payment -> approve, and in case failure: process payment failed -> cancel product reservation -> reject order.

And in general, OrderSaga receives events, and publish command.

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
1. OrderSaga: ${products.events.topic.name} -> ProductReservedEvent, ProcessPaymentCommand -> ${payments.commands.topic.name}
# payments-service
1. ${payments.commands.topic.name} -> ProcessPaymentCommand
2. PaymentProcessedEvent or PaymentProcessingFailedEvent -> ${payments.events.topic.name}
# orders-service
1. OrderSaga: ${payments.events.topic.name} -> PaymentProcessedEvent, ApproveOrderCommand -> ${orders.commands.topic.name}
2. (OrderCommandsHandler) ${orders.commands.topic.name} -> ApproveOrderCommand, (OrderService#approveOrder) OrderApprovedEvent -> ${orders.events.topic.name}
3. OrderSaga: ${orders.events.topic.name} ->  OrderApprovedEvent

# happy flow test
1. create product: post localhost:8081/products
   {
   "name": "iPhone 31",
   "price": 800,
   "quantity": 5
   }
2. create order: post, replace the productId with the response above:
   {
   "productId": "e1294802-ea00-4f56-a1d1-cd618df0d955",
   "productQuantity": 1,
   "customerId": "a6e12149-92af-4fa9-a4ae-962b1f1e4000"
   }
3. query for an order its history: get http://localhost:8080/orders/c6725d1e-1e0f-4c37-aa45-99058a18883e/history the final result should be something like this:
   [
     {
       "id": "f0dbdc54-d965-445a-86a2-425ef9462878",
       "orderId": "aa83aeaa-efca-428a-a28a-d93934b76695",
       "status": "CREATED",
       "createdAt": "2025-04-01T19:17:27.858+00:00"
     },
     {
       "id": "54616758-b713-4862-bcfa-586a806f9aa6",
       "orderId": "aa83aeaa-efca-428a-a28a-d93934b76695",
       "status": "APPROVED",
       "createdAt": "2025-04-01T19:17:28.709+00:00"
     }
   ]
