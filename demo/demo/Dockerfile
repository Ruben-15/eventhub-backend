# Stage 1: Build the Application
# Use a base image that includes both Java 17 and Maven for the build process
FROM maven:3.9.5-eclipse-temurin-17-focal AS build

# Set the working directory for the build stage
WORKDIR /app

# Copy the necessary files for the Maven build (using your renamed folders)
# We copy the pom.xml first to allow Maven to cache dependencies
COPY pom.xml ./
COPY source-code src/

# Build the application (Skipping tests for a faster, stable deployment)
# This command runs successfully because the base image includes the 'mvn' executable.
RUN mvn clean install -DskipTests -Dmaven.test.skip=true

# Stage 2: Create the Final Runtime Image
# Use a smaller JRE image for the final runtime (Java Runtime Environment)
FROM eclipse-temurin:17-jre-focal

# Set the working directory for the runtime image
WORKDIR /app

# 🛑 CRITICAL FIX: Copy the built JAR from the 'build' stage
# The JAR is named demo-0.0.1-SNAPSHOT.jar by Maven.
# We copy it to the root of the /app directory and name it app.jar.
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port Spring Boot runs on (Default 8080)
EXPOSE 8080

# The command to run the application when the container starts
# CRITICAL FIX: We run the application using the new, correct path and name: /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
