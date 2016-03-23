#!/bin/bash

if [ "$1" != "--skipGradle" ]; then
    ./gradlew clean build
fi
docker build -t beresfordt/insultbot .
