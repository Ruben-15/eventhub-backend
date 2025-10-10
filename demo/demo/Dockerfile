# Use a base image that includes both Java 17 and Maven
FROM maven:3.9.5-eclipse-temurin-17-focal AS build

# Set the working directory
WORKDIR /app

# Copy the necessary files for the Maven build
# Maven will handle the build, so we only need pom.xml and source code
COPY pom.xml ./
COPY source-code src/

# Build the application (Skipping tests)
RUN mvn clean install -DskipTests -Dmaven.test.skip=true

# --- Start a second stage for the runtime environment (smaller image) ---
FROM eclipse-temurin:17-jre-focal

# Set the working directory
WORKDIR /app

# Copy the built JAR from the 'build' stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# The command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
