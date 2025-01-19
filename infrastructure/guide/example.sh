#!/bin/bash

# in ~/.bashrc, define alias to speedup:
# alias kafka-configs='kafka-configs.sh --bootstrap-server localhost:9092,localhost:9094'
# alias kafka-console-consumer='kafka-console-consumer.sh --bootstrap-server localhost:9092,localhost:9094'
# alias kafka-topics='kafka-topics.sh --bootstrap-server localhost:9092,localhost:9094'

# start natively installed kafka:
export KAFKA_LOG4J_OPTS="-Dlog4j.configuration=file:/etc/kafka/log4j.properties"
kafka-storage.sh random-uuid
kafka-storage.sh format -t X2edeaTuRSCYe1J8t93G0Q -c /etc/kafka/kraft/server.properties
kafka-server-start.sh /etc/kafka/kraft/server.properties 

# stop natively installed kafka:
kafka-server-stop.sh

# create a tppic,
# when --partitions not specified, then num.partitions property is used (typical value is 1)
# --replication-factor: how many copies of each partition are stored across different brokers
# --bootstrap-server: it is recommended to provid at least 2, so that when one is offline, the other could process the request
# --min.insync.replicas: how many replicas must be in sync before a write is considered successful:
# When a producer sends a message with acks=all (ensuring the message is written to all in-sync replicas),
# this setting enforces how many replicas must be in-sync for the write to succeed.
kafka-topics.sh --create --topic test-topic --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092,localhost:9094 --config min.insync.replicas=2
# list topics: display only names of the topics
kafka-topics.sh --list --bootstrap-server localhost:9092
# run list topics command from host of a docker compose file
docker compose exec kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
# describe a topic: display more information than --list, e.g., its leader broker, etc.
kafka-topics.sh --describe --bootstrap-server localhost:9092 test-topic2
# delete a topic
kafka-topics.sh --delete --topic topic1 --bootstrap-server localhost:9092
# update a topic:
kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name topic2 --add-config retention.ms=43200000
# parititions could ONLY be increased, NOT decreased
kafka-topics.sh --bootstrap-server localhost:9092 --alter --topic topic2 --partitions 6

# send a message to a topic
kafka-console-producer.sh --bootstrap-server localhost:9092,localhost:9096 --topic topic2 
# send a message to a topic, note that when a topic does not exist, it will create a new one by default (can be configured to disable it).
kafka-console-producer.sh --bootstrap-server localhost:9092,localhost:9096 --topic topic2 --property parse.key=true --property key.separator=:

# consume a message from a topic
kafka-console-consumer --topic topic2 --from-beginning --property print.key=true --property print.value=true
