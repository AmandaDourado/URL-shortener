FROM amazoncorretto:25-jdk AS build

COPY target/URL-shortener-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]

