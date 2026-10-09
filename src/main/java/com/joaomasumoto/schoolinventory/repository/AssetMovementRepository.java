package com.joaomasumoto.schoolinventory.repository;

import com.joaomasumoto.schoolinventory.domain.entities.AssetMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetMovementRepository extends JpaRepository<AssetMovement, Long> {
}
