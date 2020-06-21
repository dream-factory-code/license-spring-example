FROM adoptopenjdk/openjdk8
COPY ./target/sample-1.0-jar-with-dependencies.jar /tmp/
WORKDIR  /tmp
ENTRYPOINT  ["java", "-jar", "sample-1.0-jar-with-dependencies.jar"]
