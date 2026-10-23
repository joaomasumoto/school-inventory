package com.joaomasumoto.schoolinventory.domain.entities;

import com.joaomasumoto.schoolinventory.domain.enums.AcquisitionMethod;
import com.joaomasumoto.schoolinventory.domain.enums.AssetStatus;
import com.joaomasumoto.schoolinventory.util.validation.StringValidator;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset_seq")
    @SequenceGenerator(name = "asset_seq", sequenceName = "asset_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalDate entryDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AcquisitionMethod acquisitionMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetStatus status;

    private String notes;

    @Column(unique = true)
    private String assetNumber;

    private String documentNumber;


    protected Asset() {

    }

    public Asset(String name, AcquisitionMethod acquisitionMethod, AssetStatus status, Location location) {
        this.name = StringValidator.validateName(name, "Asset name", 50);
        this.acquisitionMethod = Objects.requireNonNull(acquisitionMethod, "Acquisition method is required.");
        this.status = Objects.requireNonNull(status, "Asset status is required.");
        this.location = validateLocation(location);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public AcquisitionMethod getAcquisitionMethod() {
        return acquisitionMethod;
    }

    public AssetStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Location getLocation() {return location;}

    private static Location validateLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("New assets must have an first location.");
        }
        if (!location.getActive()) {
            throw new IllegalArgumentException("Inactives locations cannot receive new assets");
        }

        return location;
    }


}
