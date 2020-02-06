FROM openjdk:8

MAINTAINER Nikita Ermakov <fontankrovi777@gmail.com>

ARG JAR_NAME
ENV JAR_NAME ${JAR_NAME:-smoke-cloud-SNAPSHOT}

EXPOSE 8080
COPY build/libs/$JAR_NAME.jar /usr/src/app
CMD java -jar /usr/src/app/$JAR_NAME.jar