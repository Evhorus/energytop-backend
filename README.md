# EnergyTop

Para la creación de esta API se utilizaron las siguientes tecnologías:

1. Spring Boot Framework: Facilita la creación de aplicaciones Java al proporcionar una estructura de proyecto predefinida y configuraciones por defecto, lo que acelera el desarrollo y simplifica la implementación de aplicaciones web.

2. Spring Boot DevTools: Permite una experiencia de desarrollo más ágil al proporcionar capacidades de reinicio automático y soporte para el desarrollo en caliente, lo que reduce el tiempo de espera durante el ciclo de desarrollo.

3. PostgreSQL: Este sistema de gestión de bases de datos relacional se utilizó para almacenar y gestionar los datos de la aplicación de manera eficiente, ofreciendo características avanzadas como transacciones y soporte para datos geoespaciales.

4. Lombok: Esta biblioteca se utilizó para reducir el código boilerplate, generando automáticamente métodos comunes como getters, setters y constructores, lo que mejora la legibilidad y mantenimiento del código.


5. JPA (Java Persistence API): Se utilizó para gestionar la persistencia de datos, permitiendo una interacción sencilla con la base de datos a través de objetos Java, lo que simplifica el acceso y manipulación de los datos.

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
     - **Puerto**: `5432`
   - Nota: Si cambiaste las credenciales en el archivo `docker-compose.yml`, utiliza las credenciales correspondientes.

### 4. Ejecutar el comando SQL


 - Ejecuta el siguiente comando SQL en el administrador de bases de datos (DBeaver, TablePlus o MySQL Workbench).

 ```
CREATE EXTENSION IF NOT EXISTS unaccent;
 ```

### 5. Verificar que la aplicación esté corriendo
   - Puedes usar Postman, Insomnia o cualquier cliente HTTP para hacer solicitudes a los endpoints de la aplicación y verificar que el backend esté funcionando correctamente.
   - Ejemplo: Realiza una solicitud GET a `http://localhost:8080/api/health` para comprobar el estado de la API.


### Ingresar al sistema 

  - El usuario por defecto es: admin@admin.com
  - La contraseña por defecto es: admin_password

## Comandos Útiles de Docker

### Detener y eliminar contenedores, redes y volúmenes
```bash
docker-compose down -v

