FROM openjdk:17
COPY "./target/NequiBackend-0.0.1-SNAPSHOT.jar" "nequi-backend.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "nequi-backend.jar"]