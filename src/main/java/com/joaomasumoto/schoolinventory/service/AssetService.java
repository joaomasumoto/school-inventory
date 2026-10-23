package com.joaomasumoto.schoolinventory.service;

import com.joaomasumoto.schoolinventory.domain.entities.Asset;
import com.joaomasumoto.schoolinventory.domain.entities.Location;
import com.joaomasumoto.schoolinventory.domain.enums.AcquisitionMethod;
import com.joaomasumoto.schoolinventory.domain.enums.AssetStatus;
import com.joaomasumoto.schoolinventory.repository.AssetRepository;
import com.joaomasumoto.schoolinventory.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final LocationRepository locationRepository;

    public AssetService(AssetRepository assetRepository, LocationRepository locationRepository) {
        this.assetRepository = assetRepository;
        this.locationRepository = locationRepository;
    }

    public Asset createAsset(String name, AcquisitionMethod acquisitionMethod, AssetStatus status, Long locationId) {
        Location location = locationRepository.findById(locationId).orElseThrow(() -> new IllegalArgumentException("Location not found."));
        Asset asset = new Asset(name, acquisitionMethod, status, location);

        return assetRepository.save(asset);
    }


}
