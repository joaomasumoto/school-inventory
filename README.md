# School Inventory

School Inventory is a REST API project for managing the assets of a school unit, developed with Java and Spring Boot.

The project was inspired by a real problem observed in a public school: the asset inventory is usually consolidated at specific moments of the year, which makes it difficult to keep information about assets, their locations and their history continuously updated.

The goal is to create a living inventory that allows the administrative team to track school assets throughout their lifecycle.

## Problem

Traditional inventory records provide a snapshot of the school's assets at a specific point in time.

This can make it difficult to:

- Keep asset information continuously updated;
- Quickly identify where an asset is located;
- Track the movement history of an asset;
- Store relevant information about its acquisition and documentation;
- Track assets that are under repair, loaned, inactive or discarded.

## Scope

The system focuses on movable physical assets belonging to the school.

Consumable and office supplies are outside the project scope, except when they have an official asset identification number.

The system is intended primarily for the school's administrative and management staff.

## Domain Model

The initial domain is based on three main concepts:

### Asset

Represents a physical asset controlled by the school.

Examples of information associated with an asset:

- Name/description;
- Entry date;
- Acquisition method;
- Status;
- Notes;
- Asset identification number (when available);
- Invoice, delivery record or other document number (when available).

### Location

Represents a physical location registered within the school, such as a classroom, office, auditorium or storage room.

A location may contain multiple assets.

### Asset Movement

Represents a location event in an asset's lifecycle.

A movement records information such as:

- Asset;
- Movement type;
- Date;
- Origin;
- Destination;
- Notes.

An asset can have multiple movements throughout its lifecycle.

Its movement records form its location history, avoiding the need to store a separate history field.

Origins and destinations may be absent depending on the movement type. For example, an asset entering the school from an external source has no internal origin, while an asset permanently leaving the school has no internal destination.

## Domain Decisions

### Asset Status

The asset status classification was based on the terminology used in the
school inventory process, with some adaptations to better represent the
requirements of a continuously managed inventory.

The current statuses are:

- `OPERATING`: the asset is functional and available for use.
- `UNDER_MAINTENANCE`: the asset is currently undergoing maintenance or repair.
- `IDLE`: the asset is not currently in use, regardless of whether it remains functional.
- `UNUSABLE`: the asset is no longer in suitable condition for use.
- `LOANED`: the asset has temporarily left the school's control through a loan.
- `DISCARDED`: the asset has permanently left the active inventory after disposal.

Some classifications from the original inventory process were intentionally
not represented as separate statuses:

- **Obsolete:** obsolescence does not necessarily determine the asset's current
  operational situation. An obsolete asset may still be in use (`OPERATING`)
  or may be unused while awaiting a future decision (`IDLE`).
- **Unserviceable / unsuitable for use:** classifications with equivalent
  behavior in the scope of this application were consolidated into `UNUSABLE`
  to avoid distinctions that would not affect the system's behavior.

`LOANED` and `DISCARDED` were added to support the continuous tracking of
assets beyond the periodic inventory process.

These statuses represent the asset's current administrative situation.
They are independent from movement types, which represent events in the
asset's location history.

## Business Rules

The following rules describe the intended domain behavior and are not yet enforced by the implementation:

- Every movement belongs to a single asset;
- An asset can have multiple movements;
- Internal transfers have both an origin and a destination;
- External entries may not have an origin;
- Assets leaving the school temporarily or permanently may not have a destination registered in the school;
- Locations represent physical places managed by the school;
- External locations, such as repair shops, are not registered as school locations;
- Asset movement history must be preserved rather than overwritten.

These rules may evolve as the project is implemented and new domain requirements are identified.

## Technologies

The project currently declares the following stack:

- Java 21
- Spring Boot 4.1.1
- Maven (Maven Wrapper included)
- Spring Web MVC
- Spring Data JPA
- PostgreSQL JDBC driver

PostgreSQL connectivity and JPA mappings are not yet configured.

## Project Status

🚧 **Under development**

The project is currently in the domain modeling and initial implementation stage.

The repository contains the Spring Boot application skeleton, initial `Asset`, `Location` and `AssetMovement` classes, and enums with defined values for acquisition methods, asset statuses and movement types. REST endpoints, persistence mappings and business rule validation are still to be implemented.

The first version will focus on a small REST API before introducing additional infrastructure and tooling.

## Building the Project

Install JDK 21 and ensure `JAVA_HOME` points to it. Use the included Maven Wrapper to compile the current sources:

```bash
# Linux / macOS
./mvnw compile
```

```powershell
# Windows PowerShell
.\mvnw.cmd compile
```

The first build requires internet access to download Maven and project dependencies.

Running the application or its generated Spring context test also requires a configured PostgreSQL datasource. Database setup and complete run instructions will be added with the persistence implementation.

## Roadmap

- [ ] Domain model
- [ ] REST API
- [ ] PostgreSQL persistence
- [ ] Validation and exception handling
- [ ] Unit tests with JUnit and Mockito
- [ ] API documentation with OpenAPI / Swagger
- [ ] Docker and Docker Compose
- [ ] Cloud deployment
- [ ] CI/CD pipeline
- [ ] Asynchronous messaging

## Motivation

This project is both a practical solution to a real administrative problem and a learning project focused on backend development with Java.

Instead of building all features at once, the application is being developed incrementally, with an emphasis on understanding architectural decisions, domain modeling, testing and backend engineering practices.
