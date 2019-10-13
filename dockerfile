FROM java:8-jdk-alpine
COPY ./build/libs/thanosbot-0.0.1-SNAPSHOT.war /usr/app/
WORKDIR /usr/app
EXPOSE 80
ENTRYPOINT ["java", "-jar", "thanosbot-0.0.1-SNAPSHOT.war"]