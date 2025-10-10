# Use a Java base image (LTS version 17, matching your pom.xml)
FROM eclipse-temurin:17-jdk-focal

# Set the working directory
WORKDIR /app

# Copy the necessary files for the Maven build (using your renamed folders)
COPY mvn-wrapper .mvn
COPY mvnw pom.xml ./

# 🛑 CRITICAL FIX: Grant execute permission to the Maven Wrapper script
RUN chmod +x mvnw

# Copy the source code (the 'source-code' folder into the required 'src' path)
COPY source-code src/

# Build the application using the wrapper script, which now has execute permission
RUN ./mvnw clean install -DskipTests -Dmaven.test.skip=true

# Expose the port Spring Boot runs on
EXPOSE 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
