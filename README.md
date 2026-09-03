# Classroom Microservice

Classroom microservice manages classrooms, as part of the Classrooms application — a distributed, event-driven system built with a microservices architecture (see full [technical documentation](#) for details).

The entire Classrooms application is deployed and available at [www.book-your-classroom.com](https://www.book-your-classroom.com).

## Table of Contents
- [Purpose](#purpose)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started (Docker)](#getting-started-docker)
- [Configuration and environment variables](#configuration-and-environment-variables)
- [Tests](#tests)
- [Limitations of deploying Classroom on its own](#limitations-of-deploying-classroom-on-its-own)
- [Exploring the Classroom Microservice](#exploring-the-classroom-microservice)
- [Swagger](#swagger)
- [Contribution and License](#contribution-and-license)

## Purpose

The purpose of this repository is to allow exploring the microservice individually, as opposed to as a part of the entire application. The accompanying database and Kafka Docker containers come with no pre-loaded data.


## Features

- Creates classrooms 
- Deletes classrooms 
- Updates classrooms  
- Retrieves list of all classrooms 

\* Note on Authentication:
All endpoints require a valid JWT with an admin role.

For convenience in this demo project, an endpoint is provided to generate a test JWT. The generated token includes all required claims, a mock email address, and a 60-minute expiration time.


## Tech Stack

- Java 17
- Spring Boot 3.4.5
- MySQL
- Kafka
- Docker
- JJWT
- Maven

**Testing**
- JUnit
- Mockito
- Testcontainers

**Internal dependency**
- `classroom-shared-library` — shared DTOs, JWT validation, Kafka event payloads and an authentication base filter. This library is fetched automatically by Maven via JitPack from the library's GitHub repository.


## Getting Started (Docker)

**Prerequisite:** Docker Desktop only.

This process builds the application (Maven and Java run inside the build container — nothing needs to be installed locally) and starts MySQL, Kafka, and the microservice together.

```bash
# 1. Clone the repository
git clone https://github.com/JCasasLopez/classroom-service

# 2. Navigate to the project directory
cd classroom-service

# 3. Build and start all services
docker compose up --build
```

## Configuration and environment variables

This project externalizes configuration (Kafka topic names and other Kafka configuration, JWT secret and database credentials) into environment variables, resolved via a `.env` file consumed by Docker Compose.

For convenience, the `.env` file is committed to this repository with demo values, so the project can be started with a single `docker-compose up` command, with no manual setup required.

This is a deliberate decision for a demo/portfolio project, not an oversight. In a real production environment:

- The `.env` file would **not** be committed to version control.
- Secrets (JWT key, database credentials) would be stored in a secure, access-controlled system designed for that purpose, instead of a plain text file, and injected into each service only at deploy time.

Since no real infrastructure or user data is involved here, the values in `.env` are safe to expose publicly, and doing so keeps the project easy to run for anyone evaluating it.

## Tests

Since this microservice focuses on CRUD operations and data flow, the test suite prioritizes integration testing. To optimize execution time, Testcontainers is implemented via the Singleton Container Pattern, utilizing lightweight base classes (BaseIntegrationTest for full context with MySQL and Kafka, and BaseRepositoryTest for MySQL-only data slice tests). This setup is complemented by web-slice tests and targeted unit tests.

Integration tests use **Testcontainers**, so they spin up their own isolated MySQL and Kafka instances — no manual setup required, and they don't interfere with the `docker-compose.yml` environment used for manual exploration.

Tests are skipped during the Docker build (`-DskipTests`) to keep `docker-compose up` fast for manual exploration of the running application. To run the full test suite:

```bash
mvn test
```

## Limitations of deploying Classroom on its own

As already mentioned, the deployment described above only allows working with the Classroom microservice on its own — the rest of the Classrooms application is left out, which, obviously makes it impossible to work with data from other microservices (User or Booking).

Note that while the technical documentation details a production deployment using replication factor of 3 for fault tolerance, this single-service setup uses **only 1 Kafka broker** for simplicity (hence a replication factor of 1).

You can inspect the classrooms Kafka topic (which is empty until an event is emitted, as it is deployed with no pre-loaded data):

```bash
# 1. Enter the Kafka container
docker exec -it kafka-broker bash

# 2. Read messages from the beginning
kafka-console-consumer --bootstrap-server localhost:9092 --topic classrooms --from-beginning
```

## Exploring the Classroom Microservice

<details>
<summary><b>Generate a valid JWT</b></summary>


To generate a token
```bash
curl --location 'http://localhost:8500/classroom/generate-token'
```

This token must be used in the `Authorization: Bearer <VALID_JWT>` header for all other endpoints below. 

</details>

<details>  
<summary><b>Create a classroom</b></summary>

```bash
curl --location 'http://localhost:8500/classroom/classrooms' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <VALID_JWT>' \
--data '{
  "name": "101",
  "seats": 40,
  "projector": true,
  "speakers": false
}'
```
</details>

<details>  
<summary><b>Update a classroom</b></summary>

```bash
curl --location --request PUT 'http://localhost:8500/classroom/classrooms/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <VALID_JWT>' \
--data '{
  "name": "Marie Curie",
  "seats": 35,
  "projector": false,
  "speakers": false
}'
```
</details>


<details>  
<summary><b>Delete a classroom</b></summary>

```bash
curl --location --request DELETE 'http://localhost:8500/classroom/classrooms/1' \
--header 'Authorization: Bearer <VALID_JWT>' 
```
</details>

<details>  
<summary><b>Get classrooms list</b></summary>

```bash
curl --location --request GET 'http://localhost:8500/classroom/classrooms' \
--header 'Authorization: Bearer <VALID_JWT>'
```
</details>

## Swagger

Interactive API documentation is available via Swagger UI:

[http://localhost:8500/classroom/swagger-ui/index.html](http://localhost:8500/classroom/swagger-ui/index.html)

This allows you to explore and try out all endpoints directly from the browser, without needing `curl`.

## Contribution and License
### Contributing
As this project is intended as a personal demo, external contributions are not being accepted at this time.

### License
This project is licensed under the MIT License.  
See the [LICENSE](./LICENSE) file for details.

## Contact
Created by Jorge Casas López.  
Email: [j.casas.lopez.26@gmail.com](mailto:j.casas.lopez.26@gmail.com) 
