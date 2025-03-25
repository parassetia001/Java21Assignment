# Use OpenJDK base image
FROM openjdk:8-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the correct JAR file from the target directory
COPY target/Java21Assignment-1.0-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
