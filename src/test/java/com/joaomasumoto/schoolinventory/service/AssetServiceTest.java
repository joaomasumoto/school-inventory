package com.joaomasumoto.schoolinventory.service;

import com.joaomasumoto.schoolinventory.domain.entities.Asset;
import com.joaomasumoto.schoolinventory.domain.entities.Location;
import com.joaomasumoto.schoolinventory.domain.enums.AcquisitionMethod;
import com.joaomasumoto.schoolinventory.domain.enums.AssetStatus;
import com.joaomasumoto.schoolinventory.repository.AssetRepository;
import com.joaomasumoto.schoolinventory.repository.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class AssetServiceTest {

    AssetService assetService;
    AssetRepository assetRepository;
    LocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        assetRepository = Mockito.mock(AssetRepository.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        assetService = new AssetService(assetRepository, locationRepository);
    }

    @Test
    void createAsset_ShouldSaveAsset_WhenLocationIsActive() {
        Location location = new Location("Room");

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(assetRepository.save(Mockito.any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Asset newAsset = assetService.createAsset("Chair", AcquisitionMethod.SEDUC, AssetStatus.OPERATING, 1L);
        assertNotNull(newAsset);

        Mockito.verify(assetRepository).save(newAsset);
        assertSame(location, newAsset.getLocation());
    }

    @Test
    void createAsset_ThrowException_WhenLocationDoesNotExist() {

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> assetService.createAsset("Chair", AcquisitionMethod.SEDUC, AssetStatus.OPERATING, 1L));
        Mockito.verify(assetRepository, Mockito.never()).save(Mockito.any(Asset.class));
    }


    @Test
    void createAsset_ThrowsException_WhenLocationIsInactive() {
        Location location = new Location("Room");
        location.deactivate();

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Assertions.assertThrows(IllegalArgumentException.class, () -> assetService.createAsset("Chair", AcquisitionMethod.SEDUC, AssetStatus.OPERATING, 1L));
        Mockito.verify(assetRepository, Mockito.never()).save(Mockito.any(Asset.class));
    }

}
