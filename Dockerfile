FROM openjdk:8

MAINTAINER Nikita Ermakov <fontankrovi777@gmail.com>

ARG JAR_NAME
ENV JAR_NAME ${JAR_NAME:-smoke-cloud-SNAPSHOT}

WORKDIR /usr/src/app

EXPOSE 8080
COPY build/libs/$JAR_NAME.jar .
CMD java -jar $JAR_NAME.jar