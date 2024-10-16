# API de Franquicias Nequi - Documentación de Prueba Técnica

## Descripción General
Este proyecto es una prueba técnica para Nequi, implementando una API para la gestión de franquicias.

## Documentación de la API
- **Swagger UI:** [http://ec2-34-235-120-208.compute-1.amazonaws.com:8080/nequi/swagger-ui/index.html](http://ec2-34-235-120-208.compute-1.amazonaws.com:8080/nequi/swagger-ui/index.html)
- La interfaz Swagger UI proporciona documentación detallada de todos los endpoints disponibles.

## Conexión a la Base de Datos
- Los detalles de conexión a la base de datos se proporcionan en un archivo `.txt` separado (no incluido en este repositorio por razones de seguridad).
- La base de datos está alojada en AWS RDS.

## Despliegue Local

### Prerrequisitos
- JDK 17
- Maven
- Docker (para despliegue con contenedor)

### Opción 1: Ejecución directa

1. Clonar el repositorio:
   ```
   git clone [url-del-repositorio]
   ```

2. Navegar al directorio del proyecto:
   ```
   cd [directorio-del-proyecto]
   ```

3. Compilar el proyecto:
   ```
   ./mvnw clean package -X -e -DskipTests
   ```

4. Ejecutar la aplicación:
   - Usando un IDE: Importar y ejecutar el proyecto en tu IDE preferido.
   - Usando la línea de comandos:
     ```
     java -jar "NequiBackend-0.0.1-SNAPSHOT.jar"
     ```

### Opción 2: Despliegue con Docker

1. Asegúrate de tener Docker instalado y en ejecución en tu sistema.

2. Construye la imagen Docker:
   ```
   docker build -t nequi-api .
   ```

3. Ejecuta el contenedor:
   ```
   docker run -p 8080:8080 nequi-api
   ```

   Esto iniciará la aplicación y la hará accesible en `http://localhost:8080/nequi`.

## Pruebas
La API está actualmente alojada en AWS EC2 para propósitos de prueba en la URL proporcionada en el enlace de Swagger UI mencionado anteriormente.

## Soporte
Para cualquier pregunta o problema, por favor abre un issue en este repositorio de GitHub.

## Sugerencia 
Al terminar las pruebas informar para detener los servicios actualmente corriendo en AWS.
