# Build stage
FROM maven:3.8.4-openjdk-17 AS build
COPY pom.xml /app/
COPY src /app/src
RUN mvn -f /app/pom.xml -DskipTests clean package -B

# Run stage
FROM openjdk:17-jdk-alpine
COPY --from=build /app/target/reading-is-good*.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]