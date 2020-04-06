#!/bin/bash
echo "Launcing application pofo-api for environment : $1" 
java -jar -Dspring.profiles.active="$1" /opt/code/target/pofo-api.jar || echo "Pofo Api Server Start Failed";exit 1