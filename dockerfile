FROM eclipse-temurin:17.0.5_8-jdk
ARG JAR_FILE=target/peoplemanager-0.0.1.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]