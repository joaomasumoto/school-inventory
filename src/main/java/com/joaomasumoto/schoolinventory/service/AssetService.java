package com.joaomasumoto.schoolinventory.service;

import com.joaomasumoto.schoolinventory.domain.entities.Asset;
import com.joaomasumoto.schoolinventory.domain.entities.Location;
import com.joaomasumoto.schoolinventory.domain.enums.AcquisitionMethod;
import com.joaomasumoto.schoolinventory.domain.enums.AssetStatus;
import com.joaomasumoto.schoolinventory.dto.AssetRequest;
import com.joaomasumoto.schoolinventory.exception.BusinessConflictException;
import com.joaomasumoto.schoolinventory.exception.ResourceNotFoundException;
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
        Location location = locationRepository.findById(locationId).orElseThrow(() -> new ResourceNotFoundException("Location not found."));
        Asset asset = new Asset(name, acquisitionMethod, status, location);

        return assetRepository.save(asset);
    }

    public Asset createAsset(AssetRequest assetRequest) {
        Location location = locationRepository.findById(assetRequest.locationId()).orElseThrow(() -> new ResourceNotFoundException("Location not found."));

        Asset asset = new Asset(assetRequest.name(), location, assetRequest.entryDate(), assetRequest.acquisitionMethod(), assetRequest.status(), assetRequest.notes(), assetRequest.assetNumber(), assetRequest.documentNumber());
        if (asset.getAssetNumber() != null) {
            if (assetRepository.existsByAssetNumber(asset.getAssetNumber())) {
                throw new BusinessConflictException("Asset Number already used.");
            }
        }
        return assetRepository.save(asset);
    }


}
