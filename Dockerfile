# Multi-stage build for Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom and source
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variables (override at runtime)
ENV DB_URL=jdbc:mysql://localhost:3306/telemedicine_db
ENV DB_USER=root
ENV DB_PASSWORD=
ENV PORT=8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
