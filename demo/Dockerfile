# Use a base image with Java installed
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the executable JAR file into the container
COPY target/your-application.jar /app/your-application.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "your-application.jar"]