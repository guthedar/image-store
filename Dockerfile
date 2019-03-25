FROM java:8
LABEL maintainer=“chathuranga.t@gmail.com”
VOLUME /tmp
EXPOSE 8080
ADD target/file-demo-0.0.1-SNAPSHOT.jar file-demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","file-demo-0.0.1-SNAPSHOT.jar"]