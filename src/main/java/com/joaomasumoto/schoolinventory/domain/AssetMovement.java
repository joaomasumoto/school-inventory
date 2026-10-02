package com.joaomasumoto.schoolinventory.domain;

import com.joaomasumoto.schoolinventory.domain.enums.MovementType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class AssetMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType type;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_location_id")
    private Location origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_location_id")
    private Location destination;

    private String notes;

    protected AssetMovement() {
    }

    public AssetMovement(Asset asset, MovementType type, LocalDate date, Location origin, Location destination, String notes) {
        this.asset = asset;
        this.type = type;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.notes = notes;

        validateMovement();
    }

    public Long getId() {
        return id;
    }

    public Asset getAsset() {
        return asset;
    }

    public MovementType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public Location getOrigin() {
        return origin;
    }

    public Location getDestination() {
        return destination;
    }

    public String getNotes() {
        return notes;
    }

    private void validateMovement() {

        if (asset == null) {
            throw new IllegalArgumentException("Asset is required");
        }

        if (type == null) {
            throw new IllegalArgumentException("Movement type is required");
        }

        if (date == null) {
            throw new IllegalArgumentException("Movement date is required");
        }

        switch (type) {
            case EXTERNAL_ENTRY:
                if (origin != null || destination == null) {
                    throw new IllegalArgumentException(
                            "EXTERNAL_ENTRY requires no origin and a destination"
                    );
                }
                break;

            case INTERNAL_TRANSFER:
                if (origin == null || destination == null) {
                    throw new IllegalArgumentException(
                            "INTERNAL_TRANSFER requires an origin and a destination"
                    );
                }
                break;

            case TEMPORARY_EXIT:
                if (origin == null || destination != null) {
                    throw new IllegalArgumentException(
                            "TEMPORARY_EXIT requires an origin and no destination"
                    );
                }
                break;

            case RETURN:
                if (origin != null || destination == null) {
                    throw new IllegalArgumentException(
                            "RETURN requires no origin and a destination"
                    );
                }
                break;

            case DISPOSAL:
                if (origin == null || destination != null) {
                    throw new IllegalArgumentException(
                            "DISPOSAL requires an origin and no destination"
                    );
                }
                break;
        }
    }

}