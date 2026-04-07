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

## Profiles and Environments

- `application.properties` → base settings
- `application-development.properties` → PostgreSQL development
- `application-production.properties` → PostgreSQL production
- `application-test.properties` (src/test/resources) → H2 for tests

Active profile controlled by environment variable:

```bash
ENVIRONMENT=development
```

Use:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

or set variable in `.env`:

```bash
ENVIRONMENT=development
```

## Docker usage

A Docker setup is included:
- `Dockerfile`
- `docker-compose.yml` (app + PostgreSQL)

Run:

```bash
docker-compose up --build
```

App: http://localhost:8080
DB: postgres://localhost:${DATABASE_PORT}/${DATABASE_NAME}

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



## Purpose

This project demonstrates backend fundamentals:

* REST API design
* Data validation
* Error handling
* Basic analytics

