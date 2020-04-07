FROM maven:3-jdk-8-openj9
ARG profile=dev
ADD ./pom.xml pom.xml
ADD ./src src
ADD ./startup.sh startup.sh
RUN chmod +x startup.sh
EXPOSE 5555
ENTRYPOINT ["/startup.sh"]
CMD [$profile]