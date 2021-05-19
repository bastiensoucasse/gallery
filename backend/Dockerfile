FROM maven:3.6.0-jdk-11 as build


COPY src /home/app/backend/src
COPY pom.xml /home/app/backend/
#COPY db_init.sql /home/app/database/
RUN mvn -f /home/app/backend/pom.xml clean package -DskipTests

FROM openjdk:11-jre-slim as gallery-application

COPY --from=build /home/app/backend/target/backend-0.0.1-SNAPSHOT.jar /usr/local/lib/gallery.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/usr/local/lib/gallery.jar" ]