package com.joaomasumoto.schoolinventory.repository;

import com.joaomasumoto.schoolinventory.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
