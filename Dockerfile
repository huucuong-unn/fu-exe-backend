#FROM openjdk:17-jdk
#
#COPY target/backend.jar /app/backend.jar
#
#EXPOSE 8086
#
#CMD ["java", "-jar", "/app/backend.jar"]


# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build

# Set environment variables
ARG DB_URL=$DB_URL
ARG DB_USER=$DB_USER
ARG DB_PASSWORD=$DB_PASSWORD
ARG AWS_S3_ACCESS_KEY=$AWS_S3_ACCESS_KEY
ARG AWS_S3_SECRET_KEY=$AWS_S3_SECRET_KEY

COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Stage 2: Run the application

# Set environment variables
ARG DB_URL=$DB_URL
ARG DB_USER=$DB_USER
ARG DB_PASSWORD=$DB_PASSWORD
ARG AWS_S3_ACCESS_KEY=$AWS_S3_ACCESS_KEY
ARG AWS_S3_SECRET_KEY=$AWS_S3_SECRET_KEY

WORKDIR /app
COPY --from=build /app/target/backend.jar /app/backend.jar
EXPOSE 8086
CMD ["java", "-jar", "/app/backend.jar"]
