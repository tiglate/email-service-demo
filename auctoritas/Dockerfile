FROM eclipse-temurin:17-jre-alpine

RUN apk update \
    apk upgrade

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /opt/app/auctoritas.jar

WORKDIR /opt/app
EXPOSE 9000

ENTRYPOINT ["java", "-jar", "auctoritas.jar"]

