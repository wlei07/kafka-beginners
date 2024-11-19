## Setup log file position:
open file /usr/local/kafka_2.13-3.6.1/config/kraft/server.properties, update:
log.dirs=/usr/local/kafka_2.13-3.6.1/logs/kraft-combined-logs

## Start Kafka without zookeeper:
bin/kafka-storage.sh random-uuid
kafka-storage.sh format -t <uuid> -c ../config/kraft/server.properties
kafka-server-start.sh ../config/kraft/server.properties

## Start Kafka with zookeeper:
### Start zookeeper:
zookeeper-server-start.sh ~/dev/kafka_2.13-3.6.1/config/zookeeper.properties
### Start Kafka:
./kafka-server-start.sh ./../config/server.properties
zookeeper-server-start.sh ../config/zookeeper.properties 

## list topics
### list topics in upstash cluster:
kafka-topics.sh --command-config /usr/local/kafka_2.13-3.6.1/bin/playground.config --bootstrap-server logical-cat-7023-eu2-kafka.upstash.io:9092 --list
### list topics locally:
kafka-topics.sh --bootstrap-server localhost:9092 --list

## create topic:
kafka-topics.sh --command-config /usr/local/kafka_2.13-3.6.1/bin/playground.config --bootstrap-server logical-cat-7023-eu2-kafka.upstash.io:9092 --create --topic <topic-name> --partitions 5 --replication-factor 2

## describe topic in detail:
kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --describe

## produce message:
kafka-console-producer.sh --producer.config /usr/local/kafka_2.13-3.6.1/config/playground.config --bootstrap-server logical-cat-7023-eu2-kafka.upstash.io:9092 --topic first_topic --property parse.key=true --property key.separator=: --producer-property acks=all --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner
>example key:example value
>name:lei

## consume message:
kafka-console-consumer.sh --consumer.config /usr/local/kafka_2.13-3.6.1/config/playground.config --bootstrap-server logical-cat-7023-eu2-kafka.upstash.io:9092 --topic second_topic --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true   --group my-secone-application --from-beginning

## list consumer group:
kafka-consumer-groups.sh --command-config /usr/local/kafka_2.13-3.6.1/config/playground.config --bootstrap-server logical-cat-7023-eu2-kafka.upstash.io:9092 --list

## describe consumer group:
kafka-consumer-groups.sh --command-config /usr/local/kafka_2.13-3.6.1/config/playground.config --bootstrap-server logical-cat-7023-eu2-kafka.upstash.io:9092 --describe --group my-secone-application

## reset consumer group:
kafka-consumer-groups.sh --command-config /usr/local/kafka_2.13-3.6.1/config/playground.config --bootstrap-server logical-cat-7023-eu2-kafka.upstash.io:9092 --group my-first-application --reset-offsets --to-earliest --topic first_topic --dry-run(--execute)