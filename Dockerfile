FROM openjdk:8

MAINTAINER Nikita Ermakov <fontankrovi777@gmail.com>

RUN if ["x$JAR_NAME"="x"]; then export JAR_NAME=smoke-cloud-SNAPSHOT; fi

WORKDIR /usr/src/app
EXPOSE 8080
COPY build/libs/$JAR_NAME.jar .
CMD java -jar $JAR_NAME.jar