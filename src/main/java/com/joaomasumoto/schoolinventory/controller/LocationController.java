package com.joaomasumoto.schoolinventory.controller;

import com.joaomasumoto.schoolinventory.domain.entities.Location;
import com.joaomasumoto.schoolinventory.dto.LocationRequest;
import com.joaomasumoto.schoolinventory.dto.LocationResponse;
import com.joaomasumoto.schoolinventory.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/locations")
public class LocationController {
    private final LocationService service;
    public LocationController(LocationService service) {this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Location>> findAll() {
        List<Location> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/active")
    public ResponseEntity<List<Location>> findAllActive() {
        List<Location> list = service.findAllActive();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Location> findLocationById(@PathVariable Long id) {
        Location location = service.findLocationById(id);
        return ResponseEntity.ok().body(location);
    }

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@RequestBody LocationRequest locationRequest) {
        Location newLocation = service.createLocation(locationRequest.name());
        LocationResponse response = new LocationResponse(
                newLocation.getId(),
                newLocation.getName(),
                newLocation.getActive()
        );

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newLocation.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }


}
