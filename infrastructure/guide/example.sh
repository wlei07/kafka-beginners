#!/bin/bash

# start natively installed kafka:
kafka-storage.sh random-uuid
kafka-storage.sh format -t X2edeaTuRSCYe1J8t93G0Q -c /etc/kafka/kraft/server.properties
export KAFKA_LOG4J_OPTS="-Dlog4j.configuration=file:/etc/kafka/log4j.properties"
kafka-server-start.sh /etc/kafka/kraft/server.properties 

# stop natively installed kafka:
kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092

# create a tppic,
# when --partitions not specified, then num.partitions property is used (typical value is 1)
kafka-topics.sh --create --topic test-topic --partitions  --bootstrap-server localhost:9092
# list topics
kafka-topics.sh --list --bootstrap-server localhost:9092
# run list topics command from host of a docker compose file
docker compose exec kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
# describe a topic, its leader broker, etc.
kafka-topics.sh --describe --bootstrap-server localhost:9092 test-topic2
