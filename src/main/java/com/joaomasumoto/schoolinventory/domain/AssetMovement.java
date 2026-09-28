package com.joaomasumoto.schoolinventory.domain;

import com.joaomasumoto.schoolinventory.domain.enums.MovementType;

import java.time.LocalDate;

public class AssetMovement {
    private int id;
    private Asset asset;
    private MovementType type;
    private LocalDate date;
    private Location origin;
    private Location destination;
    private String notes;



}
