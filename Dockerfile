# --- ETAPA 1: Build ---
FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app

# Copiamos solo el pom.xml primero para aprovechar la caché de Docker
COPY pom.xml .
# Descargamos dependencias (esto se guarda en caché si no se cambia el pom)
RUN mvn dependency:go-offline

# Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# --- ETAPA 2: Run ---
# Usamos JRE (Java Runtime Environment) en su versión Alpine (súper liviana)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copiamos SOLO el JAR desde la etapa "build"
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]