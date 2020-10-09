FROM java:8-jdk-alpine

COPY ./target/sck-demo-consumer-1.0.0.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch sck-demo-consumer-1.0.0.jar'

ENTRYPOINT ["java", "-jar", "sck-demo-consumer-1.0.0.jar"]