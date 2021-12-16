FROM openjdk:11.0.13-slim
ARG JAR_FILE=/build/libs/gold_dduck-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /gold-dduck.jar
ENTRYPOINT ["java","-jar","/gold-dduck.jar"]
