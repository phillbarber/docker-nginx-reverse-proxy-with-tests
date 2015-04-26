#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

IMAGE_NAME=nginx-reverse-proxy-with-tests
CONTAINER_NAME=nginx-reverse-proxy-with-tests
APPLICATION_HOST_IP=`ip route show | grep docker0 | awk '{print \$9}'`

echo "IMAGE_NAME=$IMAGE_NAME"
echo "CONTAINER_NAME=$CONTAINER_NAME"
echo "APPLICATION_HOST_IP=$APPLICATION_HOST_IP"