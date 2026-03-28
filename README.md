# Task Analytics API

A production-ready REST API for task management built with Spring Boot, Spring Data JPA, and H2 database.

## Features

✅ **Arquitectura en Capas**: Controller → Service → Repository → Entity
✅ **DTOs**: Separación clara entre API y modelo interno
✅ **Validaciones**: @NotNull, @NotBlank, @Size en DTOs
✅ **Manejo Global de Errores**: @ControllerAdvice para respuestas consistentes
✅ **Enums para Status**: Type-safe status management
✅ **JPA Auditing**: Timestamps automáticos
✅ **CRUD Completo**: Create, Read, Update, Delete
✅ **Endpoints de Estadísticas**: Stats agregadas

## Requirements

- Java 17
- Maven 3.8+

## Running the Application

1. Navigate to the project directory
2. Run: `mvn spring-boot:run`
3. The API will be available at `http://localhost:8080`

H2 Console: `http://localhost:8080/h2-console` (usuario: sa, sin contraseña)

## API Endpoints

### CRUD Operations

#### Create Task
```http
POST /tasks
Content-Type: application/json

{
  "title": "Implement authentication",
  "description": "Add JWT authentication to the API"
}
```

**Response (201):**
```json
{
  "id": 1,
  "title": "Implement authentication",
  "description": "Add JWT authentication to the API",
  "status": "TODO",
  "createdAt": "2026-03-27T10:30:00",
  "completedAt": null,
  "timeSpentMinutes": null
}
```

#### Get All Tasks
```http
GET /tasks
```

**Response (200):**
```json
[
  {
    "id": 1,
    "title": "Task 1",
    "description": "Description",
    "status": "TODO",
    "createdAt": "2026-03-27T10:30:00",
    "completedAt": null,
    "timeSpentMinutes": null
  }
]
```

#### Get Task by ID
```http
GET /tasks/{id}
```

**Response (200):**
```json
{
  "id": 1,
  "title": "Task 1",
  "description": "Description",
  "status": "TODO",
  "createdAt": "2026-03-27T10:30:00",
  "completedAt": null,
  "timeSpentMinutes": null
}
```

#### Update Task
```http
PUT /tasks/{id}
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "timeSpentMinutes": null
}
```

**Response (200):**
```json
{
  "id": 1,
  "title": "Updated title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "createdAt": "2026-03-27T10:30:00",
  "completedAt": null,
  "timeSpentMinutes": 0
}
```

#### Delete Task
```http
DELETE /tasks/{id}
```

**Response: 204 No Content**

### Statistics Endpoints

#### Completed Tasks Count
```http
GET /tasks/stats/completed
```

**Response (200):**
```json
5
```

#### Tasks by Status
```http
GET /tasks/stats/by-status
```

**Response (200):**
```json
{
  "TODO": 8,
  "IN_PROGRESS": 3,
  "DONE": 5
}
```

#### Average Time Spent (completed tasks)
```http
GET /tasks/stats/avg-time
```

**Response (200):**
```json
120.5
```

## Error Handling

The API returns consistent error responses:

### Validation Error (400)
```json
{
  "timestamp": "2026-03-27T10:35:00",
  "status": 400,
  "message": "Error de validación",
  "errors": {
    "title": "El título debe tener entre 3 y 255 caracteres",
    "status": "El estado es requerido"
  },
  "path": "/tasks"
}
```

### Not Found Error (404)
```json
{
  "timestamp": "2026-03-27T10:36:00",
  "status": 404,
  "message": "Tarea no encontrada con id: 999",
  "errors": null,
  "path": "/tasks/999"
}
```

## Data Model

```
Task
├── id (Long, auto-generated)
├── title (String, required, 3-255 chars)
├── description (String, optional, max 1000 chars)
├── status (Enum: TODO, IN_PROGRESS, DONE)
├── createdAt (LocalDateTime, auto-managed)
├── completedAt (LocalDateTime, set when status → DONE)
└── timeSpentMinutes (Integer, calculated on completion)
```

## TaskStatus Enum

```
TODO          → Nueva tarea
IN_PROGRESS   → En desarrollo
DONE          → Completada
```

## Project Structure

```
src/main/java/com/example/taskanalytics/
├── controller/
│   └── TaskController.java          # REST endpoints
├── service/
│   └── TaskService.java             # Business logic
├── repository/
│   └── TaskRepository.java          # Data access
├── model/
│   ├── Task.java                    # Entity
│   └── TaskStatus.java              # Enum
├── dto/
│   ├── TaskResponseDTO.java         # Response DTO
│   ├── CreateTaskDTO.java           # Create request
│   └── UpdateTaskDTO.java           # Update request
├── exception/
│   ├── GlobalExceptionHandler.java  # Centralized error handling
│   ├── ResourceNotFoundException.java
│   └── ErrorResponse.java
└── TaskAnalyticsApiApplication.java # Main class
```

## Validation Rules

- **title**: Required, 3-255 characters
- **description**: Optional, max 1000 characters
- **status**: Required, must be TODO/IN_PROGRESS/DONE

## Technologies

- Spring Boot 3.2.0
- Spring Data JPA
- Jakarta Persistence API
- H2 Database
- Maven