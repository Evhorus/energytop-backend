
# Producción
# # Etapa de construcción
# FROM eclipse-temurin:21.0.4_7-jdk

# # Informar el puerto donde se ejecuta el contenedor
# EXPOSE 8080

# # Definir directorio raiz de nuestro contenedor
# WORKDIR /app

# # Copiar y pegar archivos dentro del contenedor

# COPY ./pom.xml /app
# COPY ./.mvn /app/.mvn
# COPY ./mvnw /app

# # Descargar las dependencias dentro del contenedor
# RUN ./mvnw dependency:go-offline


# # Copiar codigo fuente del contenedor
# COPY ./src /app/src


# # Contruir aplicacion se le pone -DskipTests porque no inclute tests
# RUN ./mvnw clean install -DskipTests

# # Usar spring-boot:run para habilitar recarga automática
# CMD ["./mvnw", "spring-boot:run"]

# Product
# ENTRYPOINT ["java", "-jar", "/app/target/energytop-backend-0.0.1-SNAPSHOT.jar"] 



# Desarrollo
# Etapa de construcción con base JDK de Eclipse Temurin
FROM eclipse-temurin:21.0.4_7-jdk

# Configurar directorio de trabajo
WORKDIR /app

# Copiar los archivos de configuración de Maven, que no cambian a menudo
COPY ./pom.xml /app
COPY ./.mvn /app/.mvn
COPY ./mvnw /app

# Descargar las dependencias, pero sin copiar el código aún.
# Esto crea una capa en caché que evita descargar las dependencias nuevamente si solo cambia el código.
RUN ./mvnw dependency:go-offline

# Copiar el código fuente en una capa separada
COPY ./src /app/src

# Usar spring-boot:run en lugar de compilar el JAR para permitir la recarga automática
CMD ["./mvnw", "spring-boot:run"]
