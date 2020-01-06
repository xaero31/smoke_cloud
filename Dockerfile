FROM openjdk:8

MAINTAINER Nikita Ermakov <fontankrovi777@gmail.com>

WORKDIR /usr/src/app

ARG JAR_NAME
RUN if ["x$JAR_NAME"="x"]; then export JAR_NAME=smoke-cloud-SNAPSHOT; else echo jar name exists; fi

EXPOSE 8080
COPY build/libs/$JAR_NAME.jar .
CMD java -jar $JAR_NAME.jar