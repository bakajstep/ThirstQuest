package edu.vutfit.ThirstQuest.dto;

import java.util.UUID;

public class WaterBubblerIdsDTO {
    private UUID bubblerId;
    private Long openStreetId;

    // Gettery a settery
    public UUID getBubblerId() {
        return bubblerId;
    }

    public WaterBubblerIdsDTO setBubblerId(UUID bubblerId) {
        this.bubblerId = bubblerId;
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
