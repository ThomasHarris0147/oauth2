FROM openjdk:11
FROM maven:3.8.5-jdk-11
WORKDIR /app
COPY ./ /app
# Make port 8080 available to the world outside container
ENV PORT 8080
EXPOSE 8080

# Package the file
RUN mvn package -DskipTests

# running stage
ENTRYPOINT ["java","-jar","./target/backend-0.0.1-SNAPSHOT.jar"]