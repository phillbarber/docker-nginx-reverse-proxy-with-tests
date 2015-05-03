#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

source common_variables.sh

cd docker 

echo "Stopping and killing any old containers...."
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true

echo "Running Image: " $IMAGE_NAME
docker run -d -p 80:80 --name $CONTAINER_NAME  --add-host application-host:$APPLICATION_HOST_IP "$IMAGE_NAME"