FROM maven:3-jdk-8-openj9
ARG profile=dev
COPY ./pom.xml /opt/code/pofo/pom.xml
COPY ./src /opt/code/pofo/src
COPY ./startup.sh /opt/code/pofo/startup.sh
RUN chmod +x /opt/code/pofo/startup.sh
EXPOSE 5555
ENTRYPOINT ["/opt/code/pofo/startup.sh"]
CMD [$profile]