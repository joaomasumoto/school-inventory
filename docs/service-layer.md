# Service Layer

[Back to project overview](../README.md)

## Current Structure

Entities now live in `domain.entities`, while enums remain in `domain.enums`. Repository imports have been updated accordingly.

`LocationService` is a Spring `@Service` with constructor injection of `LocationRepository`. `AssetService` and `AssetMovementService` are empty placeholders and do not yet implement use cases.

## Location Operations

| Method | Current behavior |
| --- | --- |
| `createLocation(name)` | Normalizes and validates the name, checks for duplicates and returns the saved entity. |
| `updateLocation(id, newName)` | Finds the entity, validates and normalizes the new name, checks for duplicates excluding its own ID, then renames and saves it. |
| `findLocationById(id)` | Returns the entity or throws `IllegalArgumentException("Location not found")`. |
| `deactivateLocation(id)` | Marks the entity inactive and saves it without deleting it. |
| `findAll()` | Lists all locations, including inactive ones. |
| `findAllActive()` | Delegates to `findByActiveTrue()`. |

Locations start with `active = true`. The non-nullable boolean field records whether the location is active; `rename` and `deactivate` expose explicit domain changes. Deactivation retains the row and its historical references. Lookup and rename currently also accept inactive locations.

`LocationService` uses `existsByName` on creation and `existsByNameAndIdNot` on update. Duplicate names raise `IllegalArgumentException` with a descriptive message; keeping the current location's own name is allowed. `findByName` is declared but not used by the service. Checks include inactive locations and do not explicitly ignore case. The unique database constraint remains necessary to handle concurrent requests.

## Name Validation

`Location.normalizeName` rejects null or blank names, removes leading and trailing whitespace with `strip()`, and rejects names longer than 50 characters after normalization. Both the constructor and `rename` use this method, so invalid renames leave the existing name unchanged. The service also normalizes names before checking uniqueness.

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
- Update returns the renamed location when the new name is available and verifies the save call.
- A duplicate name raises `IllegalArgumentException`, preserves the original name and does not call `save` with any location.
- A parameterized update test uses `@MethodSource` to reject null, empty, whitespace-only and 51-character names. Each case checks the exception, preservation of the original name and absence of a `save` call for that location.

- Deactivation changes an existing location from active to inactive and verifies the save call.
- Deactivation throws `IllegalArgumentException` when the location does not exist.
- `findAll` returns the repository list and handles an empty list.
- `findAllActive` delegates to `findByActiveTrue`, returns its list and handles an empty list. These tests verify service delegation, not database filtering.

Test names follow `methodName_ExpectedBehavior_Scenario`.

The class currently contains 21 implemented test executions (including eight executions from two parameterized tests), with no empty test methods. All 21 passed in the latest isolated service test run. This does not imply exhaustive coverage of every behavior.

Further scenarios, such as accepting exactly 50 characters, trimming names on update, retaining a location's own name and including inactive locations in the `findAll` test data, remain to be added. Mock-based tests do not verify database query behavior, uniqueness constraints or transactions.
