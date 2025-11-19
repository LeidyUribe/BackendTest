# Franchise API

API REST con Spring Boot 3 y Java 17 para gestionar franquicias, sucursales y productos.

## 📋 Tabla de Contenidos

- [Requisitos](#requisitos)
- [Arquitectura](#arquitectura)
- [Modelo de Datos](#modelo-de-datos)
- [Servicios Expuestos](#servicios-expuestos)
- [Cómo Ejecutar](#cómo-ejecutar)
- [Documentación API](#documentación-api)
- [Tecnologías](#tecnologías)

---

## Requisitos

- Java 17
- Maven 3.9+
- Docker / Docker Compose
- Acceso a una instancia MySQL 8+

---

## Arquitectura

### Patrón Arquitectónico

La aplicación sigue una **arquitectura en capas (Layered Architecture)** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────┐
│                    Controllers Layer                      │
│  (FranchiseController, FranchiseReactiveController)      │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                    Services Layer                        │
│  (FranchiseService, BranchService, ProductService)      │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                  Repositories Layer                       │
│  (FranchiseRepository, BranchRepository, ProductRepo)   │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                    Database Layer                         │
│                      MySQL 8+                            │
└─────────────────────────────────────────────────────────┘
```

### Capas de la Aplicación

#### 1. **Controllers (Capa de Presentación)**
- **Responsabilidad**: Manejar las peticiones HTTP y respuestas
- **Componentes**:
  - `FranchiseController`: Endpoints REST tradicionales (síncronos)
  - `FranchiseReactiveController`: Endpoints reactivos (WebFlux)
- **Tecnologías**: Spring Web MVC, Spring WebFlux

#### 2. **Services (Capa de Lógica de Negocio)**
- **Responsabilidad**: Implementar la lógica de negocio y validaciones
- **Componentes**:
  - `FranchiseService`: Gestión de franquicias
  - `BranchService`: Gestión de sucursales
  - `ProductService`: Gestión de productos y consultas de stock
- **Patrones**: Service Interface + Implementation, Transaction Management

#### 3. **Repositories (Capa de Acceso a Datos)**
- **Responsabilidad**: Abstracción del acceso a la base de datos
- **Componentes**:
  - `FranchiseRepository`: Operaciones CRUD de franquicias
  - `BranchRepository`: Operaciones CRUD de sucursales
  - `ProductRepository`: Operaciones CRUD de productos
- **Tecnologías**: Spring Data JPA, Hibernate

#### 4. **Entities (Capa de Dominio)**
- **Responsabilidad**: Representar el modelo de datos
- **Componentes**:
  - `Franchise`: Entidad de franquicia
  - `Branch`: Entidad de sucursal
  - `Product`: Entidad de producto
- **Tecnologías**: JPA, Hibernate ORM

#### 5. **DTOs (Data Transfer Objects)**
- **Responsabilidad**: Transferir datos entre capas sin exponer entidades
- **Componentes**:
  - **Request DTOs**: `FranchiseRequest`, `BranchRequest`, `ProductRequest`, `RenameRequest`, `StockUpdateRequest`
  - **Response DTOs**: `FranchiseResponse`, `BranchResponse`, `ProductResponse`, `MaxStockResponse`

#### 6. **Mappers**
- **Responsabilidad**: Convertir entre Entidades y DTOs
- **Componentes**: `FranchiseMapper`, `BranchMapper`, `ProductMapper`
- **Tecnologías**: MapStruct

#### 7. **Exception Handling**
- **Responsabilidad**: Manejo centralizado de excepciones
- **Componentes**:
  - `GlobalExceptionHandler`: Manejo global de excepciones
  - `BusinessException`: Excepciones de negocio
  - `ResourceNotFoundException`: Recurso no encontrado

### Principios de Diseño Aplicados

- **Separación de Responsabilidades (SoC)**: Cada capa tiene una responsabilidad única
- **Inversión de Dependencias (DIP)**: Los servicios dependen de interfaces, no de implementaciones
- **Single Responsibility Principle (SRP)**: Cada clase tiene una única responsabilidad
- **DRY (Don't Repeat Yourself)**: Uso de Lombok y MapStruct para reducir código repetitivo
- **Transaction Management**: Transacciones declarativas con `@Transactional`

---

## Modelo de Datos

### Relaciones entre Entidades

```
Franchise (1) ────────< (N) Branch (1) ────────< (N) Product
```

- **Franchise** (Franquicia)
  - `id`: Long (PK, BIGINT)
  - `name`: String (único, máximo 120 caracteres)
  - Relación: Una franquicia tiene muchas sucursales (One-to-Many)

- **Branch** (Sucursal)
  - `id`: Long (PK, BIGINT)
  - `name`: String (máximo 120 caracteres)
  - `franchise_id`: Long (FK, BIGINT)
  - Relación: Pertenece a una franquicia (Many-to-One)
  - Relación: Una sucursal tiene muchos productos (One-to-Many)

- **Product** (Producto)
  - `id`: Long (PK, BIGINT)
  - `name`: String (máximo 120 caracteres)
  - `stock`: Integer (no negativo)
  - `branch_id`: Long (FK, BIGINT)
  - Relación: Pertenece a una sucursal (Many-to-One)

### Características del Modelo

- **Cascada**: Eliminación en cascada de sucursales al eliminar franquicia
- **Orphan Removal**: Eliminación automática de productos huérfanos
- **Lazy Loading**: Carga diferida de relaciones para optimizar consultas
- **Entity Graphs**: Optimización de consultas con `@NamedEntityGraph`

---

## Servicios Expuestos

### Base URL

**Cuando se ejecuta con Docker Compose:**
```
http://localhost:8081
```

**Cuando se ejecuta localmente con Maven:**
```
http://localhost:8080
```

### 📌 Endpoints de Franquicias

#### 1. Crear Franquicia
```http
POST /franquicias
Content-Type: application/json

{
  "name": "Nombre de la Franquicia"
}
```

**Respuesta 201 Created:**
```json
{
  "id": 1,
  "name": "Nombre de la Franquicia"
}
```

---

#### 2. Renombrar Franquicia
```http
PATCH /franquicias/{id}
Content-Type: application/json

{
  "name": "Nuevo Nombre"
}
```

**Respuesta 200 OK:**
```json
{
  "id": 1,
  "name": "Nuevo Nombre"
}
```

---

### 🏢 Endpoints de Sucursales

#### 3. Agregar Sucursal a una Franquicia
```http
POST /franquicias/{id}/sucursales
Content-Type: application/json

{
  "name": "Nombre de la Sucursal"
}
```

**Respuesta 201 Created:**
```json
{
  "id": 1,
  "name": "Nombre de la Sucursal",
  "franchiseId": 1
}
```

---

#### 4. Renombrar Sucursal
```http
PATCH /franquicias/{id}/sucursales/{branchId}
Content-Type: application/json

{
  "name": "Nuevo Nombre de Sucursal"
}
```

**Respuesta 200 OK:**
```json
{
  "id": 1,
  "name": "Nuevo Nombre de Sucursal",
  "franchiseId": 1
}
```

---

### 📦 Endpoints de Productos

#### 5. Agregar Producto a una Sucursal
```http
POST /franquicias/{id}/sucursales/{branchId}/productos
Content-Type: application/json

{
  "name": "Nombre del Producto",
  "stock": 100
}
```

**Respuesta 201 Created:**
```json
{
  "id": 1,
  "name": "Nombre del Producto",
  "stock": 100,
  "branchId": 1
}
```

---

#### 6. Renombrar Producto
```http
PATCH /franquicias/{id}/sucursales/{branchId}/productos/{productId}
Content-Type: application/json

{
  "name": "Nuevo Nombre del Producto"
}
```

**Respuesta 200 OK:**
```json
{
  "id": 1,
  "name": "Nuevo Nombre del Producto",
  "stock": 100,
  "branchId": 1
}
```

---

#### 7. Actualizar Stock de un Producto
```http
PATCH /franquicias/{id}/sucursales/{branchId}/productos/{productId}/stock
Content-Type: application/json

{
  "stock": 200
}
```

**Respuesta 200 OK:**
```json
{
  "id": 1,
  "name": "Nombre del Producto",
  "stock": 200,
  "branchId": 1
}
```

---

#### 8. Eliminar Producto
```http
DELETE /franquicias/{id}/sucursales/{branchId}/productos/{productId}
```

**Respuesta 204 No Content**

---

#### 9. Obtener Productos con Stock Máximo por Sucursal
```http
GET /franquicias/{id}/productos-max-stock
```

**Respuesta 200 OK:**
```json
[
  {
    "branchId": 1,
    "branchName": "Sucursal Norte",
    "productId": 1,
    "productName": "Hamburguesa",
    "maxStock": 120
  },
  {
    "branchId": 2,
    "branchName": "Sucursal Sur",
    "productId": 3,
    "productName": "Helado",
    "maxStock": 150
  }
]
```

**Nota**: Si una sucursal no tiene productos, se retorna con `productId: null`, `productName: null` y `maxStock: 0`.

---

### ⚡ Endpoints Reactivos (WebFlux)

#### 10. Obtener Todas las Franquicias (Reactivo)
```http
GET /reactivo/franquicias
```

**Respuesta 200 OK:**
```json
[
  {
    "id": 1,
    "name": "Franquicia Central"
  },
  {
    "id": 2,
    "name": "Franquicia Este"
  }
]
```

---

#### 11. Obtener Franquicia por ID (Reactivo)
```http
GET /reactivo/franquicias/{id}
```

**Respuesta 200 OK:**
```json
{
  "id": 1,
  "name": "Franquicia Central"
}
```

---

### 📊 Resumen de Endpoints

| Método | Endpoint | Descripción | Código |
|--------|----------|-------------|--------|
| POST | `/franquicias` | Crear franquicia | 201 |
| PATCH | `/franquicias/{id}` | Renombrar franquicia | 200 |
| POST | `/franquicias/{id}/sucursales` | Agregar sucursal | 201 |
| PATCH | `/franquicias/{id}/sucursales/{branchId}` | Renombrar sucursal | 200 |
| POST | `/franquicias/{id}/sucursales/{branchId}/productos` | Agregar producto | 201 |
| PATCH | `/franquicias/{id}/sucursales/{branchId}/productos/{productId}` | Renombrar producto | 200 |
| DELETE | `/franquicias/{id}/sucursales/{branchId}/productos/{productId}` | Eliminar producto | 204 |
| PATCH | `/franquicias/{id}/sucursales/{branchId}/productos/{productId}/stock` | Actualizar stock | 200 |
| GET | `/franquicias/{id}/productos-max-stock` | Productos con stock máximo | 200 |
| GET | `/reactivo/franquicias` | Todas las franquicias (reactivo) | 200 |
| GET | `/reactivo/franquicias/{id}` | Franquicia por ID (reactivo) | 200 |

---

### 🔍 Manejo de Errores

La API retorna códigos de estado HTTP estándar:

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **204 No Content**: Recurso eliminado exitosamente
- **400 Bad Request**: Error de validación o negocio
- **404 Not Found**: Recurso no encontrado
- **422 Unprocessable Entity**: Error de validación de datos
- **500 Internal Server Error**: Error interno del servidor

**Formato de Error:**
```json
{
  "timestamp": "2025-01-18T16:42:56.893-05:00",
  "status": 404,
  "error": "Not Found",
  "message": "Franquicia no encontrada: 999",
  "path": "/franquicias/999/productos-max-stock"
}
```

---

## Cómo Ejecutar

### Requisitos Previos

- **Docker** y **Docker Compose** instalados
- **Java 17** y **Maven 3.9+** (solo si ejecutas sin Docker)

### Opción 1: Docker Compose (Recomendado)

Esta es la forma más sencilla de ejecutar toda la aplicación con un solo comando.

#### 1. Levantar los servicios

```bash
docker-compose up -d
```

Este comando:
- Construye la imagen de la aplicación Spring Boot
- Inicia el contenedor de MySQL 8.3
- Inicia el contenedor de la aplicación
- Crea el usuario `franchise_user` automáticamente mediante el script `docker/init.sql`

#### 2. Verificar el estado

```bash
# Ver estado de los contenedores
docker-compose ps

# Ver logs de la aplicación
docker-compose logs app

# Ver logs de la base de datos
docker-compose logs db
```

#### 3. Acceder a la aplicación

Una vez que los contenedores estén en ejecución:
- **API Base URL**: `http://localhost:8081`
- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **MySQL**: `localhost:3307` (usuario: `franchise_user`, contraseña: `franchise_pass`)

#### 4. Detener los servicios

```bash
# Detener los contenedores
docker-compose down

# Detener y eliminar volúmenes (elimina los datos de la base de datos)
docker-compose down -v
```

#### Notas importantes:

- **Puertos**: 
  - La aplicación corre en el puerto **8081** (mapeado desde 8080 del contenedor)
  - MySQL corre en el puerto **3307** (mapeado desde 3306 del contenedor)
  - Si estos puertos están en uso, puedes cambiarlos en `docker-compose.yml`

- **Base de datos**: 
  - El script `docker/init.sql` se ejecuta automáticamente al crear el contenedor por primera vez
  - Los datos persisten en el volumen `db_data` incluso si detienes los contenedores

- **Variables de entorno**:
  - La aplicación se conecta a MySQL usando el nombre del servicio `db` (resolución DNS interna de Docker)
  - Las credenciales están configuradas en `docker-compose.yml`

### Opción 2: Maven (Desarrollo local)

Requiere tener MySQL corriendo localmente o en otro contenedor.

```bash
# Ejecutar la aplicación
./mvnw spring-boot:run
```

**Nota**: Asegúrate de que MySQL esté corriendo en `localhost:3306` con la base de datos `franchise_db` y el usuario `franchise_user` configurado.

### Opción 3: Docker (Solo aplicación)

Si ya tienes MySQL corriendo y solo quieres ejecutar la aplicación en Docker:

```bash
# Construir la imagen
docker build -t accenture-app .

# Ejecutar el contenedor
docker run -p 8081:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/franchise_db \
  -e SPRING_DATASOURCE_USERNAME=franchise_user \
  -e SPRING_DATASOURCE_PASSWORD=franchise_pass \
  accenture-app
```

### Opción 4: JAR Ejecutable

```bash
# Compilar el proyecto
./mvnw clean package

# Ejecutar el JAR
java -jar target/accenture-1.0.0.jar
```

**Nota**: Requiere tener MySQL configurado y corriendo localmente.

---

## Documentación API

Una vez que la aplicación esté ejecutándose, puedes acceder a:

**Con Docker Compose:**
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs

**Con Maven local:**
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

---

## Tecnologías

### Core Framework
- **Spring Boot 3.2.5**: Framework principal
- **Java 17**: Lenguaje de programación
- **Maven**: Gestión de dependencias

### Persistencia
- **Spring Data JPA**: Abstracción de acceso a datos
- **Hibernate**: ORM (Object-Relational Mapping)
- **MySQL 8+**: Base de datos relacional

### Web
- **Spring Web MVC**: Framework web tradicional
- **Spring WebFlux**: Framework web reactivo
- **Jakarta Validation**: Validación de datos

### Utilidades
- **Lombok**: Reducción de código boilerplate
- **MapStruct**: Mapeo entre objetos
- **Spring Actuator**: Monitoreo y métricas

### Documentación
- **SpringDoc OpenAPI**: Documentación automática de API

### Testing
- **JUnit 5**: Framework de testing
- **Mockito**: Mocking framework
- **Spring Boot Test**: Testing de integración
- **Testcontainers**: Testing con contenedores
- **H2**: Base de datos en memoria para tests

### Infraestructura
- **Docker**: Containerización
- **Docker Compose**: Orquestación de contenedores
- **Terraform**: Infrastructure as Code (AWS)

---

## Estructura del Proyecto

```
src/main/java/com/test/franchise/
├── AccentureApplication.java          # Clase principal
├── config/
│   └── OpenApiConfig.java             # Configuración OpenAPI
├── controllers/
│   ├── FranchiseController.java       # Controlador REST tradicional
│   └── FranchiseReactiveController.java # Controlador reactivo
├── dto/
│   ├── request/                       # DTOs de petición
│   └── response/                      # DTOs de respuesta
├── entities/
│   ├── Franchise.java                 # Entidad Franquicia
│   ├── Branch.java                    # Entidad Sucursal
│   └── Product.java                   # Entidad Producto
├── exceptions/
│   ├── BusinessException.java         # Excepción de negocio
│   ├── ResourceNotFoundException.java # Recurso no encontrado
│   ├── ErrorResponse.java             # Respuesta de error
│   └── GlobalExceptionHandler.java    # Manejador global
├── mappers/
│   ├── FranchiseMapper.java           # Mapper de Franquicia
│   ├── BranchMapper.java              # Mapper de Sucursal
│   └── ProductMapper.java             # Mapper de Producto
├── repositories/
│   ├── FranchiseRepository.java       # Repositorio de Franquicia
│   ├── BranchRepository.java          # Repositorio de Sucursal
│   └── ProductRepository.java         # Repositorio de Producto
├── services/
│   ├── FranchiseService.java          # Interfaz de servicio
│   ├── BranchService.java             # Interfaz de servicio
│   ├── ProductService.java            # Interfaz de servicio
│   └── impl/                          # Implementaciones
│       ├── FranchiseServiceImpl.java
│       ├── BranchServiceImpl.java
│       └── ProductServiceImpl.java
└── utils/
    └── StockUtils.java                # Utilidades de stock
```

---

## Validaciones

### Franquicia
- `name`: Requerido, máximo 120 caracteres, único

### Sucursal
- `name`: Requerido, máximo 120 caracteres

### Producto
- `name`: Requerido, máximo 120 caracteres
- `stock`: Requerido, número entero no negativo (≥ 0)

---

## Notas Adicionales

- La aplicación utiliza **transacciones declarativas** con `@Transactional` en los servicios
- Las relaciones JPA utilizan **lazy loading** para optimizar el rendimiento
- Los endpoints reactivos están implementados como wrappers sobre servicios bloqueantes
- La base de datos se inicializa automáticamente con datos de ejemplo (ver `data.sql`)
- El esquema de base de datos se actualiza automáticamente con `ddl-auto: update`

---

*Desarrollado con Spring Boot 3.2.5 y Java 17*
