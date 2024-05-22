# Crea una imagen de Docker para la aplicación Spring Boot
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

COPY . .

RUN ./mvnw install -DskipTests -e

ENV DB_USER=admin

ENV DB_PASSWORD=4dm1n

ENV DB_HOST=postgres

ENV DB_PORT=5432

ENV DB_NAME=bank

ENV PORT=8080

EXPOSE $PORT
# Copy the JAR file into the container at /app
#COPY build/libs/routes.jar /app/app.jar

# Comando para ejecutar la aplicación Spring Boot
CMD ["java", "-jar", "/app/target/BankInc-1.0.0.jar"]