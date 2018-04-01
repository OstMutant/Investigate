FROM openjdk:8
COPY ./target/classes/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","org.ost.investigate.HelloWorld"]