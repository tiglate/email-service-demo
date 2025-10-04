FROM eclipse-temurin:17-jre-alpine

RUN apk update \
    apk upgrade

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /opt/app/tesserarius.jar

WORKDIR /opt/app
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "tesserarius.jar"]

