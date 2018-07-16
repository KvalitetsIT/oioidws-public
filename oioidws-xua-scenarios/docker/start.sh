#!/bin/bash

# Shutdown any running docker containers
docker-compose down

# Run sub script for compiling and building STS image
(cd sts/; ./compile-build-image.sh)

# Run sub script for building IdP image
(cd idp/; ./build-image.sh)

# Run docker containers
docker-compose up
