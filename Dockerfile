# Fase de construção
FROM maven:4.0.0-rc-5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Fase de execução
FROM eclipse-temurin:17-jre
WORKDIR /app
RUN adduser --system --group appuser
USER appuser

ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]