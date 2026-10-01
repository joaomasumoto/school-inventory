package com.joaomasumoto.schoolinventory.domain;

import com.joaomasumoto.schoolinventory.domain.enums.AcquisitionMethod;
import com.joaomasumoto.schoolinventory.domain.enums.AssetStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

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

    public Asset(String name, AcquisitionMethod acquisitionMethod, AssetStatus status) {
        this.name = name;
        this.acquisitionMethod = acquisitionMethod;
        this.status = status;
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


}
