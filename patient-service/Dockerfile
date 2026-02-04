FROM maven:3.9.11-amazoncorretto-21 AS builder

WORKDIR /app

COPY pom.xml .

# Install all dependencies and catch them
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package

FROM amazoncorretto:21.0.8 AS runner

WORKDIR /app

COPY --from=builder ./app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "app.jar"]