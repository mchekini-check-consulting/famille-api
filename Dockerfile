FROM debian
RUN apt-get update && apt-get install openjdk-11-jdk vim curl -y
WORKDIR /opt
ADD target/garde-*.jar garde-enfant.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/garde-enfant.jar"]
