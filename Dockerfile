FROM openjdk:8

MAINTAINER Nikita Ermakov <fontankrovi777@gmail.com>

WORKDIR /usr/src/app
EXPOSE 8080
COPY build/libs/smoke-cloud-0.0.1-SNAPSHOT.jar .
CMD java -jar smoke-cloud-0.0.1-SNAPSHOT.jar