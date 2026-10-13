package com.joaomasumoto.schoolinventory.service;

import com.joaomasumoto.schoolinventory.domain.entities.Location;
import com.joaomasumoto.schoolinventory.repository.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class LocationServiceTest {

    LocationRepository locationRepository;
    LocationService locationService;


    @BeforeEach
    void setUp() {
        locationRepository = Mockito.mock(LocationRepository.class);
        locationService = new LocationService(locationRepository);
    }

    @Test
    void createLocation_ReturnsSavedLocation_WhenNameIsAvailable() {
        String locationName = " Room ";
        Mockito.when(locationRepository.existsByName("Room")).thenReturn(false);
        Mockito.when(locationRepository.save(Mockito.any(Location.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Location newLocation = locationService.createLocation(locationName);

        Assertions.assertEquals("Room", newLocation.getName());
        Mockito.verify(locationRepository).save(Mockito.argThat(location -> location.getName().equals("Room")));
        Mockito.verify(locationRepository).existsByName("Room");
    }

    @Test
    void createLocation_ThrowsIllegalArgumentException_WhenNameAlreadyExists() {
        String locationName = " Room ";
        Mockito.when(locationRepository.existsByName("Room")).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.createLocation(locationName));
        Mockito.verify(locationRepository, Mockito.never()).save(Mockito.any(Location.class));
    }

    @ParameterizedTest
    @MethodSource("invalidLocationNames")
    void createLocation_ThrowsIllegalArgumentException_WhenNameIsInvalid(String name) {

        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.createLocation(name));

        Mockito.verifyNoInteractions(locationRepository);
    }

    @Test
    void createLocation_ReturnsSavedLocation_WhenNameHasExactly50Characters() {
        String boundaryLimitName = "a".repeat(50);

        Mockito.when(locationRepository.existsByName(boundaryLimitName)).thenReturn(false);
        Mockito.when(locationRepository.save(Mockito.any(Location.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Location newLocation = locationService.createLocation(boundaryLimitName);

        Assertions.assertEquals(boundaryLimitName, newLocation.getName());
        Mockito.verify(locationRepository).save(Mockito.argThat(location -> location.getName().equals(boundaryLimitName)));
        Mockito.verify(locationRepository).existsByName(boundaryLimitName);
    }

    @Test
    void updateLocation_ReturnsUpdatedLocation_WhenNewNameIsAvailable() {
        Location location = new Location("Room");
        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.existsByNameAndIdNot("Library", 1L)).thenReturn(false);
        Mockito.when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.updateLocation(1L, " Library ");

        Assertions.assertEquals("Library", result.getName());
        Mockito.verify(locationRepository).existsByNameAndIdNot("Library", 1L);
        Mockito.verify(locationRepository).save(location);
    }

    @Test
    void updateLocation_ReturnsUpdatedLocation_WhenNewNameIsTheSame() {
        Location location = new Location("Room");
        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.existsByNameAndIdNot("Room", 1L)).thenReturn(false);
        Mockito.when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.updateLocation(1L, "Room");

        Assertions.assertEquals("Room", result.getName());
        Mockito.verify(locationRepository).existsByNameAndIdNot("Room", 1L);
        Mockito.verify(locationRepository).save(location);
    }



    @Test
    void updateLocation_ThrowsIllegalArgumentException_WhenNameIsAlreadyUsed() {
        Location location = new Location("Room");
        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.existsByNameAndIdNot("Library", 1L)).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(1L, "Library"));
        Assertions.assertEquals("Room", location.getName());

        Mockito.verify(locationRepository, Mockito.never()).save(Mockito.any(Location.class));
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

        Mockito.verify(locationRepository, Mockito.never()).save(Mockito.any(Location.class));

    }

    @Test
    void updateLocation_ReturnsUpdatedLocation_WhenNameHasExactly50Characters() {
        Location location = new Location("Room");
        String boundaryLimitName = "a".repeat(50);

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.existsByNameAndIdNot(boundaryLimitName, 1L)).thenReturn(false);
        Mockito.when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.updateLocation(1L, boundaryLimitName);

        Assertions.assertEquals(boundaryLimitName, result.getName());
        Mockito.verify(locationRepository).save(location);
        Mockito.verify(locationRepository).existsByNameAndIdNot(boundaryLimitName, 1L);
    }

    @Test
    void updateLocation_ThrowsIllegalArgumentException_WhenLocationDoesNotExist() {

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.updateLocation(1L, "Room"));

        Mockito.verify(locationRepository, Mockito.never()).save(Mockito.any(Location.class));
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
    void deactivateLocation_SetsActiveToFalse_WhenLocationExists() {
        Location location = new Location("Room");

        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Assertions.assertTrue(location.getActive());

        locationService.deactivateLocation(1L);

        Assertions.assertFalse(location.getActive());
        Mockito.verify(locationRepository).save(location);
    }

    @Test
    void deactivateLocation_ThrowsIllegalArgumentException_WhenLocationDoesNotExist() {
        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> locationService.deactivateLocation(1L));

        Mockito.verify(locationRepository, Mockito.never()).save(Mockito.any(Location.class));
    }

    @Test
    void findAll_ReturnsAllLocations_WhenLocationsExist() {
        List<Location> locationList = List.of(
                new Location("Room 1"),
                new Location("Room 2"),
                new Location("Room 3")
        );

        locationList.get(1).deactivate();

        Mockito.when(locationRepository.findAll()).thenReturn(locationList);

        List<Location> result = locationService.findAll();

        Assertions.assertEquals(locationList, result);
        Mockito.verify(locationRepository).findAll();
        Assertions.assertEquals(3, result.size());
        Assertions.assertFalse(result.get(1).getActive());
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoLocationsExist() {
        Mockito.when(locationRepository.findAll()).thenReturn(List.of());

        List<Location> result = locationService.findAll();

        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(locationRepository).findAll();
    }

    @Test
    void findAllActive_ReturnsActiveLocations_WhenActiveLocationsExist() {
        List<Location> locationList = List.of(
                new Location("Room 1"),
                new Location("Room 2")
        );

        Mockito.when(locationRepository.findByActiveTrue()).thenReturn(locationList);

        List<Location> result = locationService.findAllActive();

        Assertions.assertEquals(locationList, result);
        Mockito.verify(locationRepository).findByActiveTrue();
    }

    @Test
    void findAllActive_ReturnsEmptyList_WhenNoActiveLocationsExist() {
        List<Location> locationList = List.of();

        Mockito.when(locationRepository.findByActiveTrue()).thenReturn(locationList);

        List<Location> result = locationService.findAllActive();

        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(locationRepository).findByActiveTrue();
    }

}
