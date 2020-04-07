#!/bin/bash
echo "<<< Deleting existing container pofo-api>>>"
docker rm -f pofo-api
echo "<<< Building application pofo api for environment : $1 >>>" 
mvn clean install -P"$1" || exit 1
echo "<<< Maven build successful.. doing docker build >>>"
docker build --build-arg profile="$1" . -t pofo-api || exit 1
echo "<<< Docker image created successfully, creating docker container >>>"
docker run -p 5555:5555 --name pofo-api -d pofo-api || exit 1
docker "<<< Build process completed successfully >>>"