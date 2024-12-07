FROM openjdk:17-slim

COPY target/controller-service-0.0.1-SNAPSHOT.jar controller.jar

ENV KAFKA_POWERPLANT_TOPIC=backend
ENV KAFKA_GROUP_ID=controller

ENTRYPOINT ["java", "-jar", "/controller.jar"]