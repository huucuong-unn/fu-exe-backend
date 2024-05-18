#FROM openjdk:17-jdk
#
#COPY target/backend.jar /app/backend.jar
#
#EXPOSE 8086
#
#CMD ["java", "-jar", "/app/backend.jar"]


# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/backend.jar /app/backend.jar
EXPOSE 8086
CMD ["java", "-jar", "/app/backend.jar"]
