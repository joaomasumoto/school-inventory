package com.joaomasumoto.schoolinventory.repository;

import com.joaomasumoto.schoolinventory.domain.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    Location findByName(String name);

    List<Location> findByActiveTrue();



}
