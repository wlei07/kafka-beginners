* copy /etc/kafka/kraft/server.properties to server-(1-3).properties
* sudo chown root:kafka server-*.properties
* open server-1.properties, edit
  * node.id: must bu unique for each broker
  * listeners, the ports must be unique for each broker, PLAINTEXT://:9092,CONTROLLER://:9093
  * controller.quorum.voters=1@localhost:9093,2@localhost:9095,3@localhost:9097
  * advertised.listeners=PLAINTEXT://localhost:9092,CONTROLLER://localhost:9093, the ports must be same as listeners.
  * log.dirs=/tmp/server-i/kraft-combined-logs
* run `kafka-storage.sh random-uuid`
* run `kafka-storage.sh format -t l2Ssyu_GR9mnN4Ssio7Mxw -c server-1.properties`
* run `kafka-storage.sh format -t l2Ssyu_GR9mnN4Ssio7Mxw -c server-2.properties`, note: the same cluster id, different configuration file.
* run `kafka-storage.sh format -t l2Ssyu_GR9mnN4Ssio7Mxw -c server-3.properties`, note: the same cluster id, different configuration file.
* run `export KAFKA_LOG4J_OPTS="-Dlog4j.configuration=file:/etc/kafka/log4j.properties"`
* run `kafka-server-start.sh server-(1-3).properties`, 3 times for each properties file