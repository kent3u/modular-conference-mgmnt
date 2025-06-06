FROM gradle:8.13-alpine AS build
WORKDIR /opt/app/
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:21
WORKDIR /opt/app/
ARG JAR_FILE=/opt/app/conference-gateway/build/libs/conference-gateway-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} /opt/app/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
EXPOSE 8080