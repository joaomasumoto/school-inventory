package com.joaomasumoto.schoolinventory.service;

import com.joaomasumoto.schoolinventory.domain.entities.Location;
import com.joaomasumoto.schoolinventory.repository.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest {

    LocationRepository locationRepository;
    LocationService locationService;


    @BeforeEach
    void setUp() {
        locationRepository = Mockito.mock(LocationRepository.class);
        locationService = new LocationService(locationRepository);
    }

    @Test
    void createLocation() {
    }

    @Test
    void updateLocation_ReturnsUpdatedLocation_WhenNewNameIsAvailable() {
        Location location = new Location("Room");
        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.existsByNameAndIdNot("Room2", 1L)).thenReturn(false);
        Mockito.when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.updateLocation(1L, "Room2");

        Assertions.assertEquals("Room2", result.getName());
    }

    @Test
    void updateLocation_ThrowsIllegalArgumentException_WhenNameIsAlreadyUsed() {
        Location location = new Location("Room");
        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.existsByNameAndIdNot("Library", 1L)).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(1L, "Library"));
        Assertions.assertEquals("Room", location.getName());

        Mockito.verify(locationRepository, Mockito.never()).save(location);
    }

    static Stream<String> invalidLocationNames() {
        return Stream.of(
                null,
                "",
                "  ",
                "a".repeat(51)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidLocationNames")
    void updateLocation_ThrowsIllegalArgumentException_WhenNewNameIsInvalid(String newName) {
        Location location = new Location("Room");

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(1L, newName));
        Assertions.assertEquals("Room", location.getName());

        Mockito.verify(locationRepository, Mockito.never()).save(location);

    }

    @Test
    void findLocationById_ReturnsLocation_WhenLocationExists() {
        Location location = new Location("Room");
        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Location result = locationService.findLocationById(1L);

        Assertions.assertSame(location, result);

    }

    @Test
    void findLocationById_ThrowsIllegalArgumentException_WhenLocationDoesNotExist() {

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.findLocationById(1L));

    }

    @Test
    void deactivationLocation() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllActive() {
    }
}
