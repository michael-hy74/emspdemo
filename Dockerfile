
FROM maven:3.8.6-jdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn -B package -DskipTests


FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]