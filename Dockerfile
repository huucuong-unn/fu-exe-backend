# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build

# Set build-time arguments
ARG DB_URL
ARG DB_USER
ARG DB_PASSWORD
ARG AWS_S3_ACCESS_KEY
ARG AWS_S3_SECRET_KEY

# Pass build-time arguments as environment variables
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV AWS_S3_ACCESS_KEY=${AWS_S3_ACCESS_KEY}
ENV AWS_S3_SECRET_KEY=${AWS_S3_SECRET_KEY}

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set environment variables for runtime
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV AWS_S3_ACCESS_KEY=${AWS_S3_ACCESS_KEY}
ENV AWS_S3_SECRET_KEY=${AWS_S3_SECRET_KEY}

WORKDIR /app
COPY --from=build /app/target/backend.jar /app/backend.jar
EXPOSE 8086
CMD ["java", "-jar", "/app/backend.jar"]