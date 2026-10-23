# Service Layer

[Back to project overview](../README.md)

## Current Structure

Entities now live in `domain.entities`, while enums remain in `domain.enums`. Repository imports have been updated accordingly.

`LocationService` is a Spring `@Service` with constructor injection of `LocationRepository` and `AssetRepository`. `AssetService` implements asset creation; `AssetMovementService` remains an empty placeholder.

## Location Operations

| Method | Current behavior |
| --- | --- |
| `createLocation(name)` | Normalizes and validates the name, checks for duplicates and returns the saved entity. |
| `updateLocation(id, newName)` | Finds the entity, validates and normalizes the new name, checks for duplicates excluding its own ID, then renames and saves it. |
| `findLocationById(id)` | Returns the entity or throws `IllegalArgumentException("Location not found")`. |
| `deactivateLocation(id)` | Returns immediately if already inactive; rejects locations with assigned assets; otherwise marks inactive and saves. |
| `findAll()` | Lists all locations, including inactive ones. |
| `findAllActive()` | Delegates to `findByActiveTrue()`. |

Locations start with `active = true`. The non-nullable boolean field records whether the location is active; `rename` and `deactivate` expose explicit domain changes. Deactivation retains the row and its historical references. Lookup and rename currently also accept inactive locations.

`LocationService` uses `existsByName` on creation and `existsByNameAndIdNot` on update. Duplicate names raise `IllegalArgumentException` with a descriptive message; keeping the current location's own name is allowed. `findByName` is declared but not used by the service. Checks include inactive locations and do not explicitly ignore case. The unique database constraint remains necessary to handle concurrent requests.

## Name Validation

`Location.normalizeName` delegates to the shared `StringValidator.validateName`, which rejects null or blank names, removes leading and trailing whitespace with `strip()`, and rejects names longer than 50 characters after normalization. Both the constructor and `rename` use this method, so invalid renames leave the existing name unchanged. The service also normalizes names before checking uniqueness.

## Remaining Work

The service currently has no explicit transaction covering an entire load-and-update operation. A service transaction boundary remains a possible next improvement. Integration verification of JPA mappings, database constraints, transactions and preservation of historical references remains pending.

## Unit Tests in Progress

`LocationServiceTest` uses JUnit Jupiter and a Mockito mock of `LocationRepository`. `@BeforeEach` creates a fresh mock and service instance for each execution. These tests exercise the real service logic without loading the Spring context or accessing a database.

Implemented scenarios:

- Creation returns the saved location with surrounding whitespace removed and verifies both the saved name and the duplicate lookup using the normalized name.
- Creation rejects an existing normalized name without saving any location.
- Parameterized creation rejects null, empty, whitespace-only and 51-character names without interacting with the repository.
- Update rejects a missing ID without saving any location.

- Lookup returns the same location supplied by the repository when the ID exists.
- Lookup throws `IllegalArgumentException` when the repository returns `Optional.empty()`.
- Update strips surrounding whitespace, returns the renamed location and verifies both the normalized duplicate lookup and the save call.
- A duplicate name raises `IllegalArgumentException`, preserves the original name and does not call `save` with any location.
- A parameterized update test uses `@MethodSource` to reject null, empty, whitespace-only and 51-character names. Each case checks the exception, preservation of the original name and absence of a `save` call with any location.

- Deactivation changes an existing location from active to inactive and verifies the save call.
- Deactivation throws `IllegalArgumentException` when the location does not exist and does not save any location.
- `findAll` returns the complete repository list, including an inactive location, checks the returned size and handles an empty list.
- `findAllActive` delegates to `findByActiveTrue`, returns its list and handles an empty list. These tests verify service delegation, not database filtering.

- Creation and update accept names with exactly 50 characters.
- Update allows retaining the current name and verifies that the duplicate query excludes the current ID.

Test names follow `methodName_ExpectedBehavior_Scenario`.

The class currently contains 24 implemented test executions (including eight executions from two parameterized tests), with no empty test methods. All 24 passed in the latest isolated service test run. This does not imply exhaustive coverage of every behavior.

Mock-based tests do not verify database query behavior, uniqueness constraints or transactions. Integration verification remains pending; the passing unit tests do not imply exhaustive coverage of all lifecycle rules.

## HTTP Exposure

`LocationController` now exposes listing all locations, listing active locations, lookup by ID, creation and update. Creation accepts `LocationRequest` and returns `LocationResponse` with a 201 response and a `Location` header. Update accepts the same request DTO and returns `LocationResponse` with status 200. Deactivation remains a service-only operation. See the [location API documentation](location-api.md) for routes and current HTTP-layer limitations.

## Asset Creation and Location Assignment

`AssetService.createAsset(name, acquisitionMethod, status, locationId)` finds the location, constructs an asset and saves it through `AssetRepository`. A missing location raises `IllegalArgumentException`. The entity requires a non-null active initial location. Asset creation does not yet record an initial movement or expose an HTTP endpoint.

`StringValidator.validateName` is shared by assets and locations: it rejects null/blank names, strips surrounding whitespace and enforces the supplied maximum length (50 for both entities). `Asset` also requires non-null acquisition method and status via `Objects.requireNonNull`, which throws `NullPointerException` for missing values.

`AssetRepository.existsByLocationId` supports the location deactivation guard. This query considers all assigned assets regardless of status. Already inactive locations return before this query and are not saved again. There is no explicit service transaction or locking for concurrent assignment/deactivation yet.

`AssetServiceTest` has three passing cases: creation in an active location, rejection of a missing location and rejection of an inactive location. Together with the 24 location cases, the isolated suite has 27 passing executions. The new deactivation guard for occupied locations and the already-inactive early return do not yet have dedicated tests.
