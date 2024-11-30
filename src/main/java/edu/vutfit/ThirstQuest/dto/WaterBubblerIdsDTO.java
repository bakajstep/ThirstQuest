package edu.vutfit.ThirstQuest.dto;

import java.util.UUID;

public class WaterBubblerIdsDTO {
    private UUID fountainId;
    private Long openStreetId;

    // Gettery a settery
    public UUID getFountainId() {
        return fountainId;
    }

    public WaterBubblerIdsDTO setFountainId(UUID fountainId) {
        this.fountainId = fountainId;
        return this;
    }

    public Long getOpenStreetId() {
        return openStreetId;
    }

    public WaterBubblerIdsDTO setOpenStreetId(Long openStreetId) {
        this.openStreetId = openStreetId;
        return this;
    }
}
