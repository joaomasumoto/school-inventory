package com.joaomasumoto.schoolinventory.domain;

import com.joaomasumoto.schoolinventory.domain.enums.AcquisitionMethod;
import com.joaomasumoto.schoolinventory.domain.enums.AssetStatus;

import java.time.LocalDate;

public class Asset {

    private int id;
    private String name;
    private LocalDate entryDate;
    private AcquisitionMethod acquisitionMethod;
    private AssetStatus status;
    private String notes;
    private String assetNumber;
    private String documentNumber;

    public Asset(String name, AcquisitionMethod acquisitionMethod, AssetStatus status) {
        this.name = name;
        this.acquisitionMethod = acquisitionMethod;
        this.status = status;
    }

    public int getId() {
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
