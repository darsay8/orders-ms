FROM openjdk:23-slim
WORKDIR /app
COPY target/orders-ms-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]