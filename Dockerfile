FROM openjdk:8
ADD target/pofo-app.jar pofo-app.jar
EXPOSE 5555