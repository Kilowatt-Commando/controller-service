# Powerplant Controller Service
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Kilowatt-Commando_controller-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Kilowatt-Commando_controller-service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Kilowatt-Commando_controller-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Kilowatt-Commando_controller-service)

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-highlight.svg)](https://sonarcloud.io/summary/new_code?id=Kilowatt-Commando_controller-service)

This repository contains the controller microservice.

## Testcontainers
This project uses Testcontainers to test certain functionalities. To execute the tests a Docker environment must be provided for these tests to run.

## Environment variables
The application requires the following environment variables:

| Name              | Description                                                               |
|-------------------|---------------------------------------------------------------------------|
| KAFKA_BROKER      | IP address of Kafka Broker                                                |
| KAFKA_BROKER_PORT | Port number of Kafka Broker                                               |
|KAFKA_GROUP_ID| Group id of controller service (default in Docker container: controller)  |
|KAFKA_POWERPLANT_CONTROL_TOPIC| Topic for powerplant commands (default in Docker container: powerplant    |
|KAFKA_POWERPLANT_TOPIC| Topic of powerplant status updates (default in Docker container: backend) |

## Run using Docker
Step 1:

Build the Docker image:
```shell
mvn package
docker build -t kwkmdo-controller .
```
Step 2:

Execute the Docker container with required ENV variables:
```shell
docker run \
  -p 8080:8080\
  -e KAFKA_BROKER=<broker_ip_address> \
  -e KAFKA_BROKER_PORT=<broker_port> \
  kwkmdo-controller
```
