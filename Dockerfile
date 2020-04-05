FROM openjdk:8
ARG profile=dev
COPY ./target/pofo-api.jar /opt/code/target/pofo-api.jar
COPY ./startup.sh /opt/code/pofo/startup.sh
RUN chmod +x /opt/code/pofo/startup.sh
EXPOSE 5555
ENTRYPOINT ["/opt/code/pofo/startup.sh"]
CMD [$profile]