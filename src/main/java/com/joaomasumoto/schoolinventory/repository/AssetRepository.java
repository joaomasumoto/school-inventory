package com.joaomasumoto.schoolinventory.repository;

import com.joaomasumoto.schoolinventory.domain.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    boolean existsByLocationId(Long id);
}
