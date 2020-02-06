FROM java:8

MAINTAINER Nikita Ermakov <fontankrovi777@gmail.com>

ARG JAR_NAME
ENV JAR_NAME ${JAR_NAME:-smoke-cloud-SNAPSHOT}

WORKDIR /home/
ADD build/libs/$JAR_NAME.jar smoke-cloud.jar
EXPOSE 8080

CMD [ "java", "-jar", "smoke-cloud.jar" ]