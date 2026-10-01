# Domain Model and Decisions

[Back to project overview](../README.md) | [JPA persistence mapping](jpa-mapping.md)

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

### Asset Encapsulation and Creation

The `Asset` entity is designed to control how its internal state is created and modified, rather than exposing public setters for all attributes.

#### Setters

Public setters were intentionally avoided in the initial domain model.

Most asset information, such as its name, entry date, acquisition method, notes, asset number and document number, may need to be corrected or updated during the asset's lifecycle. However, instead of exposing unrestricted setters, these changes will be introduced through explicit domain operations as the corresponding use cases are implemented.

The asset status requires additional control because a status change may represent an actual event in the asset's lifecycle rather than a simple data correction. For example, changing an asset from `OPERATING` to `DISCARDED` should not necessarily be treated as a generic field update, since the operation may also require a corresponding disposal movement.

This distinction separates:

- **Data updates:** corrections or changes to descriptive information about the asset.
- **Domain operations:** events that change the asset's state and may involve additional business rules.

The application service layer will be responsible for coordinating use cases, persistence and interactions between domain objects, while the `Asset` entity remains responsible for controlling valid changes to its own state.

#### Getters

Getters are provided to allow the application to read the current state of an `Asset` without exposing its fields directly.

The `id` is readable but does not have a public setter, since its value will be managed by the persistence mechanism rather than manually assigned by application code.

#### Asset Creation

An `Asset` requires the following information when it is created:

- `name`
- `acquisitionMethod`
- `status`

These fields represent the minimum information required for an asset to exist meaningfully in the system.

Other attributes are optional because they may legitimately be unknown, especially when registering assets that already existed before the system was introduced:

- `entryDate`: the exact date when older assets entered the school may be unknown.
- `assetNumber`: some assets may not have an official asset number.
- `documentNumber`: acquisition or delivery documentation may not be available.
- `notes`: additional information is optional.

Therefore, the initial constructor is:

```java
public Asset(
        String name,
        AcquisitionMethod acquisitionMethod,
        AssetStatus status) {
    this.name = name;
    this.acquisitionMethod = acquisitionMethod;
    this.status = status;
}
```

The `status` is explicitly required instead of automatically defaulting to `OPERATING`. A newly registered asset is not necessarily operational: it may be idle, under maintenance, or in another valid situation at the moment it is registered.

The `id` is also excluded from the constructor because it will be generated by the persistence layer when the entity is stored.

The current constructor defines the required parameters but does not yet validate null or blank values. Optional attributes currently remain unset; operations to populate or update them will be introduced with the corresponding use cases.

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
