# School Inventory

School Inventory is a REST API project for managing school assets, built with Java and Spring Boot as a study and portfolio project.

## Motivation and Scope

Inspired by a real problem observed in a public school, the project aims to replace periodic inventory snapshots with a continuously maintained record of assets, their locations and movement history.

It focuses on movable physical assets managed by administrative and management staff. Consumable and office supplies are outside the scope unless they have an official asset identification number.

## Technologies

- Java 21 and Spring Boot 4.1.1
- Spring Web MVC and Spring Data JPA / Hibernate
- PostgreSQL JDBC driver
- Maven, with Maven Wrapper included

## Current Status

🚧 **Under development** — domain modeling and initial persistence mapping.

The project contains JPA mappings for `Asset`, `Location` and `AssetMovement`, including generated IDs, field constraints and movement relationships. Movement creation validates required data and the origin/destination combination for each movement type.

Database connectivity, REST endpoints and the remaining lifecycle rules are still to be implemented. The application is being developed incrementally to explore domain modeling, testing and backend engineering decisions.

## Documentation

- [Domain model and decisions](docs/domain-model.md): scope of the domain objects, asset statuses, encapsulation, creation and intended business rules.
- [JPA persistence mapping](docs/jpa-mapping.md): entity mapping, IDs, constraints, enums and persistence constructors.

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

The first build requires internet access to download Maven and dependencies. Running the application or its generated Spring context test requires a configured PostgreSQL datasource. Complete database setup and run instructions will follow with the persistence implementation.

## Roadmap

- [ ] Complete domain model and JPA relationships
- [ ] REST API and PostgreSQL persistence
- [ ] Validation and exception handling
- [ ] Unit tests with JUnit and Mockito
- [ ] API documentation with OpenAPI / Swagger
- [ ] Docker and Docker Compose
- [ ] Cloud deployment and CI/CD
- [ ] Asynchronous messaging
