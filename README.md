# Instalación del Backend - Spring Boot

## Requisitos Previos

1. **Tener Docker instalado**.
   - Si aún no lo tienes, descarga e instala Docker Desktop desde el siguiente enlace:
     - [Descargar Docker Desktop](https://www.docker.com/products/docker-desktop/)
   - Asegúrate de que Docker esté funcionando correctamente. Puedes verificarlo con el siguiente comando en la terminal:
     ```bash
     docker --version
     ```
   - Debería mostrar la versión de Docker instalada en tu sistema.

2. **Tener Java JDK 21  instalado**.
   - Asegúrate de tener una versión compatible de JDK instalada para poder compilar y ejecutar la aplicación Spring Boot.


## Pasos para iniciar el Backend

### 1. Iniciar Docker Desktop
   - Asegúrate de que Docker Desktop se esté ejecutando en tu máquina.

### 2. Levantar los servicios con Docker Compose
   - Abre la terminal de tu IDE o cualquier otra terminal de tu preferencia y ejecuta el siguiente comando en el directorio donde se encuentra el archivo `docker-compose.yml`:
     ```bash
     docker-compose up -d
     ```
   - Este comando creará y levantará los contenedores en segundo plano basados en las definiciones del archivo `docker-compose.yml`.

### 3. Conectar la base de datos
   - Utiliza un gestor de base de datos como DBeaver, TablePlus o MySQL Workbench para conectarte a la base de datos.
   - Utiliza las siguientes credenciales por defecto:
     - **Base de datos**: `energytopDB`
     - **Usuario**: `energytop`
     - **Contraseña**: `12345`
     - **Host**: `localhost`
     - **Puerto**: `3306`
   - Nota: Si cambiaste las credenciales en el archivo `docker-compose.yml`, utiliza las credenciales correspondientes.

### 4. Verificar que la aplicación esté corriendo
   - Puedes usar Postman, Insomnia o cualquier cliente HTTP para hacer solicitudes a los endpoints de la aplicación y verificar que el backend esté funcionando correctamente.
   - Ejemplo: Realiza una solicitud GET a `http://localhost:8080/api/health` para comprobar el estado de la API.

## Comandos Útiles de Docker

### Detener y eliminar contenedores, redes y volúmenes
```bash
docker-compose down -v

