# School Inventory

School Inventory is a REST API project for managing school assets, built with Java and Spring Boot as a study and portfolio project.

## Motivation and Scope

Inspired by a real problem observed in a public school, the project aims to replace periodic inventory snapshots with a continuously maintained record of assets, their locations and movement history.

It focuses on movable physical assets managed by administrative and management staff. Consumable and office supplies are outside the scope unless they have an official asset identification number.

## Technologies

- Java 21 and Spring Boot 4.1.1
- Spring Web MVC and Spring Data JPA / Hibernate
- PostgreSQL JDBC driver and Spring Boot Validation
- Maven, with Maven Wrapper included

## Current Status

🚧 **Under development** — domain modeling and initial persistence mapping.

The project contains JPA mappings for `Asset`, `Location` and `AssetMovement`, including generated IDs, field constraints and movement relationships. Movement creation validates required data and the origin/destination combination for each movement type.

Spring Data JPA repositories and local PostgreSQL configuration are available. Entities are organized under `domain.entities`. `LocationService` supports creation, renaming, lookup, listing and deactivation, with name normalization, validation and duplicate checks; `AssetService` and `AssetMovementService` are placeholders. REST endpoints, Bean Validation annotations and the remaining lifecycle rules are still to be implemented. The application is being developed incrementally to explore domain modeling, testing and backend engineering decisions.

## Documentation

- [Domain model and decisions](docs/domain-model.md): scope of the domain objects, asset statuses, encapsulation, creation and intended business rules.
- [JPA persistence mapping](docs/jpa-mapping.md): entity mapping, IDs, constraints, enums and persistence constructors.

- [Service layer](docs/service-layer.md): implemented location operations and current limitations.

## Build

Install JDK 21 and set `JAVA_HOME`. Compile with the included Maven Wrapper:

```bash
# Linux / macOS
./mvnw compile
```

```powershell
# Windows PowerShell
.\mvnw.cmd compile
```

The first build requires internet access to download Maven and dependencies.

## Run Locally

Create a PostgreSQL database named `school_inventory` on `localhost:5432`. The default database user is `postgres`. Set the password through an environment variable before starting the application:

```powershell
# Windows PowerShell: replace the example value with your local password
$env:SPRING_DATASOURCE_PASSWORD = '<your-local-password>'
.\mvnw.cmd spring-boot:run
```

```bash
# Linux / macOS
export SPRING_DATASOURCE_PASSWORD='<your-local-password>'
./mvnw spring-boot:run
```

To use a different database address or user, also set `SPRING_DATASOURCE_URL` or `SPRING_DATASOURCE_USERNAME`.

The current development configuration uses `spring.jpa.hibernate.ddl-auto=update` and logs formatted SQL. Hibernate can update the schema when the application starts; use a local development database. Controlled schema migrations are still pending.

The generated Spring context test also requires the database and environment variables. Run it with `.\mvnw.cmd test` on Windows or `./mvnw test` on Linux / macOS.

## Unit Tests

JUnit and Mockito tests cover location creation, lookup, updates, deactivation and listings. `LocationServiceTest` currently has 21 test executions, including parameterized invalid-name cases, with no empty test methods. Additional boundary scenarios and integration tests remain pending.

Run only this isolated test class without starting Spring or connecting to PostgreSQL:

```powershell
# Windows PowerShell
.\mvnw.cmd "-Dtest=LocationServiceTest" test
```

```bash
# Linux / macOS
./mvnw -Dtest=LocationServiceTest test
```

See [service test coverage](docs/service-layer.md#unit-tests-in-progress) for the implemented scenarios and remaining work.

## Roadmap

- [ ] Complete domain model and JPA relationships
- [ ] REST API and PostgreSQL persistence
- [ ] Validation and exception handling
- [ ] Expand unit tests with JUnit and Mockito (initial location tests implemented)
- [ ] API documentation with OpenAPI / Swagger
- [ ] Docker and Docker Compose
- [ ] Cloud deployment and CI/CD
- [ ] Asynchronous messaging
