# --- ETAPA 1: Build ---
FROM maven:4.0.0-openjdk-25 AS build
WORKDIR /app
# Copiamos solo el pom.xml primero para aprovechar la caché de Docker
COPY pom.xml .
# Descargamos dependencias (esto se guarda en caché si no cambias el pom)
RUN mvn dependency:go-offline

# Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# --- ETAPA 2: Run ---
FROM openjdk:25-jdk-slim
WORKDIR /app

# Copiamos SOLO el JAR desde la etapa "build"
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]