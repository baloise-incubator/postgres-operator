FROM maven:3.6-jdk-8-alpine as builder
WORKDIR application

COPY pom.xml .
COPY src src

RUN mvn install

ARG JAR_FILE=target/*.jar
RUN mv $JAR_FILE application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:8-jre-alpine3.9
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

