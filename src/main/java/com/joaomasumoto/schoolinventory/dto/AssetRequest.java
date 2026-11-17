package com.joaomasumoto.schoolinventory.dto;

import com.joaomasumoto.schoolinventory.domain.enums.AcquisitionMethod;
import com.joaomasumoto.schoolinventory.domain.enums.AssetStatus;

import java.time.LocalDate;

public record AssetRequest(
        String name,
        AcquisitionMethod acquisitionMethod,
        AssetStatus status,
        Long locationId,
        String assetNumber,
        String documentNumber,
        LocalDate entryDate,
        String notes
) {}
