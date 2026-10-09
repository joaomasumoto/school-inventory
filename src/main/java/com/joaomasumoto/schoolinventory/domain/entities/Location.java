package com.joaomasumoto.schoolinventory.domain.entities;

import jakarta.persistence.*;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
    @SequenceGenerator(name = "location_seq", sequenceName = "location_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    protected Location() {}

    public Location(String name) {
        this.name = normalizeName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getActive() { return active; }

    public void rename(String newName) {
        this.name = normalizeName(newName);
    }

    public static String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Location name must not be null or blank.");
        }

        String normalizedName = name.strip();

        if (normalizedName.length() > 50) {
            throw new IllegalArgumentException("Location name must not exceed 50 characters.");
        }

        return normalizedName;
    }

    public void deactivate() {
        this.active = false;
    }

}
