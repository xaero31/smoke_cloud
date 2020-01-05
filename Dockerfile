FROM openjdk:8

MAINTAINER Nikita Ermakov <fontankrovi777@gmail.com>

ENV JAR_NAME=$JAR_NAME

WORKDIR /usr/src/app
EXPOSE 8080
COPY build/libs/$JAR_NAME .
CMD java -jar $JAR_NAME