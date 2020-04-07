#!/bin/bash
echo "<<< Deleting existing container pofo-api>>>"
docker rm -f pofo-stage-api
docker rmi pofo-api
echo "<<< Doing docker build with profile: $1 >>>"
docker build --build-arg profile="$1" . -t pofo-api || exit 1
echo "<<< Docker image created successfully.. run the docker now >>>"