#!/bin/bash
echo "<<< Deleting existing image of pofo-api>>>"
docker rmi pofo-api
echo "<<< Doing docker build with profile: $1 >>>"
docker build --build-arg profile="$1" . -t pofo-api || exit 1
echo "<<< Docker image created successfully.. run the docker now >>>"