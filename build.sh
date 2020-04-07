#!/bin/bash
echo "<<< Deleting existing container pofo-api>>>"
docker rm -f pofo-stage-api
echo "<<< Building application pofo api for environment : $1 >>>" 
mvn clean install -DskipTests -P"$1" || exit 1
echo "<<< Maven build successful.. doing docker build >>>"
docker build --build-arg profile="$1" . -t pofo-api || exit 1
echo "<<< Docker image created successfully >>>"
docker "<<< Build process completed successfully >>>"

