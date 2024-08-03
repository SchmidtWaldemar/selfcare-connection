# Build stage by maven
FROM maven:3.8.5-openjdk-17 AS build

# copy all required files to docker container
COPY src /app/src
COPY certs/localhost.p12 src/main/resources
COPY pom.xml /app

# start build process by maven
RUN mvn -f /app/pom.xml clean package

# Next stage by using the preferred OpenJDK base image
FROM openjdk:17-jdk-alpine

# Copy the built jar file into the container by preferred name on destination
COPY --from=build /app/target/selfcare-connection-0.0.1-SNAPSHOT.jar app.jar

# use the intern Spring Boot SSL port from Tomcat 
EXPOSE 8443

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
