FROM eclipse-temurin:21-jdk-alpine
COPY gh-application-container/build/libs/gh-application-container.jar golden-home.jar
ENTRYPOINT ["java", "-jar", "golden-home.jar"]

