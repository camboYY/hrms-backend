# Use a Maven build image to compile the app
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the app
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/attendance-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8084

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
