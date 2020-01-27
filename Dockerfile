FROM openjdk:8

MAINTAINER Nikita Ermakov <fontankrovi777@gmail.com>

ARG JAR_NAME
ARG spring_profiles_active

ENV JAR_NAME ${JAR_NAME:-smoke-cloud-SNAPSHOT}
ENV spring_profiles_active $spring_profiles_active

WORKDIR /usr/src/app

EXPOSE 8080
COPY build/libs/$JAR_NAME.jar .
CMD java -Djasypt.encryptor.password=$("cat /etc/secret/smoke-cloud-secret") -jar $JAR_NAME.jar