#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

source common_variables.sh

cd docker

echo "Building with tag: " $IMAGE_NAME
docker build -t "$IMAGE_NAME" .;