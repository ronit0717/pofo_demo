FROM openjdk:8

ARG profile=dev

COPY ./src /opt/code/pofo/src
COPY ./pom.xml  /opt/code/pofo/pom.xml
COPY ./startup.sh /opt/code/pofo/startup.sh
WORKDIR /opt/code/pofo

RUN mvn clean install -P$profile
RUN rm -rf src

CMD ["bash","startup.sh",$profile]