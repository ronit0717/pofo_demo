#!/bin/bash
echo "Building the application with environment : $1"
mvn clean install -P"$1"
echo "Launcing application pofo-api for environment : $1"
java -jar -Dspring.profiles.active="$1" /target/pofo-api.jar || echo "Pofo Api Server Start Failed";exit 1
