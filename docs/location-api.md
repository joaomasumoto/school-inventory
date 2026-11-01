# Location API

[Back to project overview](../README.md) | [Service layer](service-layer.md)

`LocationController` exposes the initial location API at `/locations` and delegates application logic to `LocationService` through constructor injection.

## Implemented Endpoints

| Method | Path | Behavior | Success status |
| --- | --- | --- | --- |
| GET | `/locations` | Lists all locations, including inactive ones. | 200 OK |
| GET | `/locations/active` | Lists active locations. | 200 OK |
| GET | `/locations/{id}` | Retrieves a location by ID. | 200 OK |
| PUT | `/locations/{id}` | Renames a location using `LocationRequest`. | 200 OK |
| PATCH | `/locations/{id}/deactivate` | Deactivates a location without deleting it. | 204 No Content |
| POST | `/locations` | Creates an active location from a name. | 201 Created |

Empty listings return an empty JSON array. The GET endpoints currently return `Location` entities directly; creation and update return a `LocationResponse` DTO. Their current JSON fields are `id`, `name` and `active`.

## Create a Location

After starting the application with PostgreSQL configured, send:

```http
POST /locations
Content-Type: application/json

{
  "name": " Library "
}
```

`LocationRequest` is a Java record containing `String name`. The service strips surrounding whitespace, validates the name and checks for duplicates before saving.

An illustrative successful response is:

```http
HTTP/1.1 201 Created
Location: http://localhost:8080/locations/1
Content-Type: application/json

{
  "id": 1,
  "name": "Library",
  "active": true
}
```

The generated ID varies. The controller builds the `Location` header from the current request URI and the saved ID. `LocationResponse` is a record containing `Long id`, `String name` and `boolean active`.

## Deactivate a Location

Send a request without a body:

```http
PATCH /locations/1/deactivate
```

On success, the endpoint returns `204 No Content` without a response body. The location remains stored with `active = false`, preserving historical references.

The service rejects an active location with assigned assets. If the location is already inactive, the operation returns successfully without querying assets or saving again. A missing location raises `ResourceNotFoundException` (404); an occupied active location raises `BusinessConflictException` (409).

## HTTP Errors

`GlobalExceptionHandler` uses `@RestControllerAdvice` to map exceptions to these responses:

| Exception | Status | Examples |
| --- | --- | --- |
| `IllegalArgumentException` | 400 Bad Request | Null, blank or overlong location names. |
| `ResourceNotFoundException` | 404 Not Found | Location ID not found during lookup, update or deactivation. |
| `BusinessConflictException` | 409 Conflict | Duplicate location name or deactivation of an occupied active location. |

Each handler returns the exception message as a string body, not a structured error DTO. For example, a duplicate location name during creation produces status 409 with the message `Location name already exists.` The mapping is implemented but has not yet been exercised by controller tests.

## Current Limitations

- Deactivation is implemented in the service but does not yet have an HTTP endpoint.
- Request DTOs do not yet have Bean Validation annotations, and the POST and PUT handlers do not use `@Valid`. Name validation is currently performed by the service/domain code.
- Database constraint exceptions and missing required asset enums (`NullPointerException`) do not have dedicated handlers. The current advice is not a complete error-handling policy.
- GET responses expose entities directly. Consistent response DTO usage across the API is still pending.
- The controller compiles and the 29 isolated service test cases pass. HTTP routing, JSON serialization, response headers and persistence against a running database have not been verified by those tests. Controller and integration tests remain pending.
