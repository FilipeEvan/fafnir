FROM ubuntu:latest AS build

WORKDIR /app

RUN apt-get update && apt-get install -y openjdk-25-jdk maven

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app
EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]