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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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
    void createAsset_NormalizesAssetNumber_WhenAssetNumberHasSurroundingWhitespace() {
        Location location = new Location("Room");

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(assetRepository.save(Mockito.any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(assetRepository.existsByAssetNumber("123")).thenReturn(false);

        String assetNumber = " 123 ";
        AssetRequest assetRequest = new AssetRequest("Chair", AcquisitionMethod.SEDUC, AssetStatus.OPERATING, 1L, assetNumber, null, LocalDate.now(), null);

        Asset newAsset = assetService.createAsset(assetRequest);

        Mockito.verify(assetRepository).save(newAsset);
        assertEquals("123", newAsset.getAssetNumber());

        Mockito.verify(assetRepository).existsByAssetNumber("123");
    }

    static Stream<String> nullableArguments() {
        return Stream.of(
                null,
                "",
                " "
        );
    }

    @ParameterizedTest
    @MethodSource("nullableArguments")
    void createAsset_SavesAssetWithoutNumber_WhenAssetNumberIsNullOrBlank(String arg) {
        Location location = new Location("Room");

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(assetRepository.save(Mockito.any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssetRequest assetRequest = new AssetRequest("Chair", AcquisitionMethod.SEDUC, AssetStatus.OPERATING, 1L, arg, null, LocalDate.now(), null);

        Asset newAsset = assetService.createAsset(assetRequest);

        Mockito.verify(assetRepository).save(newAsset);
        assertNull(newAsset.getAssetNumber());

        Mockito.verify(assetRepository, Mockito.never()).existsByAssetNumber(Mockito.nullable(String.class));
    }

    @Test
    void createAsset_ThrowsBusinessConflictException_WhenAssetNumberIsAlreadyUsed() {
        Location location = new Location("Room");

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(assetRepository.existsByAssetNumber("123")).thenReturn(true);

        String assetNumber = "123";

        Assertions.assertThrows(BusinessConflictException.class, () -> assetService.createAsset(new AssetRequest("Chair", AcquisitionMethod.SEDUC, AssetStatus.OPERATING, 1L, assetNumber, null, LocalDate.now(), null)));
        Mockito.verify(assetRepository, Mockito.never()).save(Mockito.any(Asset.class));
    }

    @Test
    void createAsset_ThrowsResourceNotFoundException_WhenLocationDoesNotExist() {

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> assetService.createAsset("Chair", AcquisitionMethod.SEDUC, AssetStatus.OPERATING, 1L));
        Mockito.verify(assetRepository, Mockito.never()).save(Mockito.any(Asset.class));
    }


    @Test
    void createAsset_ThrowsBusinessConflictException_WhenLocationIsInactive() {
        Location location = new Location("Room");
        location.deactivate();

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Assertions.assertThrows(BusinessConflictException.class, () -> assetService.createAsset("Chair", AcquisitionMethod.SEDUC, AssetStatus.OPERATING, 1L));
        Mockito.verify(assetRepository, Mockito.never()).save(Mockito.any(Asset.class));
    }

}
