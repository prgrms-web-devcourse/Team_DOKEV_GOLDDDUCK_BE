FROM openjdk:11.0.13-slim
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} gold-dduck.jar
ENTRYPOINT ["java","-jar","/gold-dduck.jar"]
