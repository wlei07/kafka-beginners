#!/bin/bash
# create a tppic:
./kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092
# list topics
./kafka-topics.sh --list --bootstrap-server localhost:9092
# run list topics command from host of a docker compose file
docker compose exec kafka-1 /opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092