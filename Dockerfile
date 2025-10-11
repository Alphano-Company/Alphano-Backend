FROM openjdk:11-jdk as builder
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN ./gradlew build -x test

FROM openjdk:11-jre-slim
WORKDIR /app

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]