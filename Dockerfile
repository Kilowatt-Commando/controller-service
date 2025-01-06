FROM openjdk:17-slim

COPY target/controller-service-0.0.1-SNAPSHOT.jar controller.jar

ENV KAFKA_POWERPLANT_TOPIC=backend
ENV KAFKA_POWERPLANT_CONTROL_TOPIC=powerplant
ENV KAFKA_GROUP_ID=controller

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/controller.jar"]