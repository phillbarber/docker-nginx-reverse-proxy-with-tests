#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

./buildDockerImage.sh
./runDockerImage.sh
./gradlew clean test