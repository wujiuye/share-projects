FROM java:8-jdk-alpine

COPY ./target/sck-demo-provider-1.0.0.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch sck-demo-provider-1.0.0.jar'

ENTRYPOINT ["java", "-jar", "sck-demo-provider-1.0.0.jar"]