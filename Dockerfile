FROM openjdk:17-jdk
WORKDIR /opt
ADD target/famille-api-*.jar famille-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/famille-api.jar"]

