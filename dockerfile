# Use an official OpenJDK runtime as a parent image
FROM openjdk:22-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the target directory to the container
COPY build/libs/BoryanasTherapy-0.0.1-SNAPSHOT.jar /app/BoryanasTherapy-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot app will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "BoryanasTherapy-0.0.1-SNAPSHOT.jar"]
