FROM openjdk:17-alpine
WORKDIR /app
COPY target/internet-banking-0.0.1-SNAPSHOT.jar internet-banking.jar
CMD ["java", "-jar", "internet-banking.jar"]