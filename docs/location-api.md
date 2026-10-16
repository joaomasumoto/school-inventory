# Location API

[Back to project overview](../README.md) | [Service layer](service-layer.md)

`LocationController` exposes the initial location API at `/locations` and delegates application logic to `LocationService` through constructor injection.

## Implemented Endpoints

| Method | Path | Behavior | Success status |
| --- | --- | --- | --- |
| GET | `/locations` | Lists all locations, including inactive ones. | 200 OK |
| GET | `/locations/active` | Lists active locations. | 200 OK |
| GET | `/locations/{id}` | Retrieves a location by ID. | 200 OK |
| POST | `/locations` | Creates an active location from a name. | 201 Created |

Empty listings return an empty JSON array. The GET endpoints currently return `Location` entities directly; creation returns a `LocationResponse` DTO. Their current JSON fields are `id`, `name` and `active`.

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

## Current Limitations

- Renaming and deactivation are implemented in the service but do not yet have HTTP endpoints.
- Request DTOs do not yet have Bean Validation annotations, and the POST handler does not use `@Valid`. Name validation is currently performed by the service/domain code.
- Missing IDs, invalid names and duplicate names raise `IllegalArgumentException` in the service/domain layer. There is no explicit HTTP exception mapping yet; clients should not assume dedicated 400, 404 or 409 responses for these cases.
- GET responses expose entities directly. Consistent response DTO usage across the API is still pending.
- The controller compiles and the 24 existing isolated service test cases pass. HTTP routing, JSON serialization, response headers and persistence against a running database have not been verified by those tests. Controller and integration tests remain pending.
