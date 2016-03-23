FROM beresfordt/alpine-java8

EXPOSE 8080
COPY build/libs/insultbot.jar /insultbot.jar

CMD ["java", "-jar", "/insultbot.jar"]