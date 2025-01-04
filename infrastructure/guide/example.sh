#!/bin/bash

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
kafka-topics.sh --create --topic test-topic --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092,localhost:9094
# list topics
kafka-topics.sh --list --bootstrap-server localhost:9092
# run list topics command from host of a docker compose file
docker compose exec kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
# describe a topic, its leader broker, etc.
kafka-topics.sh --describe --bootstrap-server localhost:9092 test-topic2
