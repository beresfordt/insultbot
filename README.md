# insultbot

Build with the ```build-docker.sh``` script to compile with Gradle, run tests and generate a Docker image named beresfordt/insultbot

Run the docker image with the following

```
 docker run -e "slackAuthToken=yourKeyHere" -e "insultUrl:http://www.insultgenerator.org/" -p 8080:8080 beresfordt/insultbot
```