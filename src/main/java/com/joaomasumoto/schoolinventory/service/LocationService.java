package com.joaomasumoto.schoolinventory.service;

import com.joaomasumoto.schoolinventory.domain.entities.Location;
import com.joaomasumoto.schoolinventory.repository.AssetRepository;
import com.joaomasumoto.schoolinventory.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final AssetRepository assetRepository;

    public LocationService(LocationRepository locationRepository, AssetRepository assetRepository) {
        this.locationRepository = locationRepository;
        this.assetRepository = assetRepository;
    }

    public Location createLocation(String name) {
        String normalizedName = Location.normalizeName(name);

        if (locationRepository.existsByName(normalizedName)) {
            throw new IllegalArgumentException("Location name already exists.");
        }

        Location location = new Location(normalizedName);

        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, String newName) {
        Location location = findLocationById(id);

        String normalizedName = Location.normalizeName(newName);

        if (locationRepository.existsByNameAndIdNot(normalizedName, id)) {
            throw new IllegalArgumentException("Another location already uses this name.");
        }

        location.rename(normalizedName);

        return locationRepository.save(location);
    }

    public Location findLocationById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        return location.orElseThrow(() -> new IllegalArgumentException("Location not found"));
    }

    public void deactivateLocation(Long id) {
        Location location = findLocationById(id);
        if (!location.getActive()) {
            return;
        }
        if (assetRepository.existsByLocationId(id)) {
            throw new IllegalArgumentException("Location cannot be deactivated while assets are assigned to it.");
        }
        location.deactivate();
        locationRepository.save(location);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public List<Location> findAllActive() {
        return locationRepository.findByActiveTrue();
    }

}
