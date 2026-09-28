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

}
