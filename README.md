# 🔐 Auth-Service

Este microservicio proporciona funcionalidades de autenticación y registro de usuarios utilizando Spring Boot, Spring Security y JWT. Es parte de un ecosistema de microservicios más grande.

## 🚀 Características

- Registro de usuarios con validación de datos únicos (email y username).
- Login de usuarios con generación de JWT.
- Protección de rutas con JWT.
- Almacenamiento de tokens en base de datos.
- Manejo de errores (usuario no encontrado, credenciales incorrectas, duplicados, etc.).
- Compatible con arquitectura basada en microservicios.

---

## 🛠️ Tecnologías usadas

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT (JSON Web Token)
- MySQL / PostgreSQL (configurable)
- Maven
- Lombok

---

## 📦 Estructura del proyecto

```bash
src/
├── config/               # Configuraciones de seguridad
├── controller/           # Controladores REST
├── dto/                  # Objetos de transferencia (DTOs)
├── model/                # Entidades JPA
├── repository/           # Repositorios JPA
├── service/              # Lógica de negocio
├── util/                 # Clases utilitarias como generadores de JWT
```

🔐 Endpoints principales
Método	Endpoint	Descripción	Protección

POST	/api/auth/register	Registrar un nuevo usuario	❌ Pública

POST	/api/auth/login	Autenticación y generación de JWT	❌ Pública

GET	/api/user/{username}	Obtener detalles del usuario	✅ Protegida


🧪 Ejemplo de AuthRequest

POST /api/auth/login
{
  "username": "teo",
  "password": "123456"
}
Respuesta

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
}

⚙️ Configuración
En application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/authdb
spring.datasource.username=root
spring.datasource.password=tu_password

jwt.secret=secreto_super_seguro
jwt.expiration=86400000
🧠 Cómo correr el proyecto

# Clonar el proyecto
git clone https://github.com/TeoLoop/AuthService.git

# Ingresar al directorio
cd auth-service

# Construir y ejecutar
./mvnw spring-boot:run

✅ TODO
 Login con JWT

 Registro de usuarios

 Validaciones de duplicados

 Almacenamiento de tokens

 Refresh token

 Recuperación de contraseña

 Roles más específicos (ADMIN, etc.)

🧑‍💻 Autor
Mateo López
📧 matelopez2830@gmail.com
🔗 [LinkedIn](https://www.linkedin.com/in/lopezmateo/)
