# Task Analytics API

REST API for task management built with Spring Boot and JPA.

## Features

* Layered architecture (Controller → Service → Repository)
* DTOs for request/response separation
* Bean Validation (@NotBlank, @Size, etc.)
* Global exception handling (@ControllerAdvice)
* Enum-based status (type-safe)
* JPA Auditing (automatic timestamps)
* CRUD operations + statistics endpoints

## Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* H2 Database
* Maven

## Run

```bash
mvn spring-boot:run
```

API: http://localhost:8080
H2 Console: http://localhost:8080/h2-console

---

## Main Endpoints

### Tasks

* `POST /tasks` → create task
* `GET /tasks` → list tasks
* `GET /tasks/{id}` → get by id
* `PUT /tasks/{id}` → update
* `DELETE /tasks/{id}` → delete

### Stats

* `GET /tasks/stats/completed`
* `GET /tasks/stats/by-status`
* `GET /tasks/stats/avg-time`

---

## Data Model

```
Task
- id (Long)
- title (String)
- description (String)
- status (TODO | IN_PROGRESS | DONE)
- createdAt
- completedAt
- timeSpentMinutes
```

---

## Structure

```
controller/   → REST endpoints
service/      → business logic
repository/   → data access
model/        → entities & enums
dto/          → API contracts
exception/    → error handling
```

---

## Notes

* `createdAt` is automatically managed via JPA Auditing
* `completedAt` and `timeSpentMinutes` are set when a task is marked as DONE
* Validation errors return consistent JSON responses

---

## Purpose

This project demonstrates backend fundamentals:

* REST API design
* Clean architecture
* Data validation
* Error handling
* Basic analytics

---
