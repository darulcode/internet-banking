FROM openjdk:17-alpine
WORKDIR /app
COPY target/mandiri-test-0.0.1-SNAPSHOT.jar mandiri-test.jar
CMD ["java", "-jar", "mandiri-test.jar"]