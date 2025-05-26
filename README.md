# Sistema Akaroa

Sistema orientado para PYMES que requieran gestionar procesos de compra/venta y llevar un control eficiente de inventario.

## Descripción

Akaroa es una aplicación desarrollada en Java 22 con Spring Boot, Spring Security, Spring Data JPA y Jakarta EE. Permite gestionar productos, clientes, ventas y usuarios con diferentes roles y permisos, facilitando el control del negocio y la seguridad de la información.

## Características principales

- Gestión integral de productos, clientes, proveedores, ventas y lotes
- Sistema de autenticación y autorización con roles (ADMIN, MANAGER, SELLER)
- Encriptación segura de contraseñas con BCrypt
- Vistas protegidas según los roles asignados a cada usuario
- CRUD completo para todas las entidades principales
- Manejo de sesiones, login y logout personalizados
- Redirección en caso de acceso denegado
- Soporte para H2, MySQL o PostgreSQL como motor de base de datos

## Tecnologías utilizadas

- Java 22
- Spring Boot
- Spring Security
- Spring Data JPA
- Jakarta EE
- Thymeleaf (para vistas)
- Maven o Gradle
- Base de datos relacional (H2/MySQL/PostgreSQL)

## Inicio rápido

1. **Clona el repositorio**
   ```bash
   git clone <URL-del-repositorio>
   cd akaroa
   ```

2. **Configura el archivo de propiedades**  
   Ajusta tu conexión a la base de datos en `src/main/resources/application.properties` según el motor que utilices.

3. **Compila y ejecuta la aplicación**
   - Con Maven:
     ```bash
     ./mvnw spring-boot:run
     ```

   - Con Gradle:
     ```bash
     ./gradlew bootRun
     ```

4. **Accede al sistema**  
   Abre tu navegador y entra a [http://localhost:8080](http://localhost:8080)

## Estructura del proyecto

- `security/` — Configuración de seguridad y gestión de roles
- `controllers/` — Controladores para manejo de vistas y APIs
- `repositories/` — Operaciones de acceso a datos con JPA
- `services/` — Lógica de negocio
- `models/` — Entidades de dominio

## Contribución

1. Realiza un fork del proyecto
2. Crea una rama nueva (`git checkout -b feature/nueva-funcionalidad`)
3. Realiza tus cambios y haz commit (`git commit -am 'Descripción del cambio'`)
4. Haz push a tu rama (`git push origin feature/nueva-funcionalidad`)
5. Envía un Pull Request

## Licencia

Este proyecto está bajo la licencia MIT.