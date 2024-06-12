FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/usermanagementsystem-0.0.1.jar
COPY ${JAR_FILE} usermanagement.jar
EXPOSE 1010
ENTRYPOINT ["java", "-jar", "usermanagement.jar"]