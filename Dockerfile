FROM adoptopenjdk/openjdk8
COPY ./target/sample-2.1.0-jar-with-dependencies.jar /tmp/
WORKDIR  /tmp
ENTRYPOINT  ["java", "-jar", "sample-2.0.2-jar-with-dependencies.jar"]
