FROM maven:3.8.1-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/email-list.jar .
ENV PORT=8080
EXPOSE 8080
CMD ["java", "-jar", "email-list.jar"]
