FROM bellsoft/liberica-openjdk-debian:21
VOLUME /tmp
ARG JAR_FILE=target/invoicing-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} invoicing.jar
ENTRYPOINT ["java","-jar","/invoicing.jar"]
