package edu.vutfit.ThirstQuest.dto;

import java.util.UUID;

public class FavoriteBubblerDTO {
    private UUID waterBubblerId;
    private Long waterBubblerOsmId;

    public UUID getWaterBubblerId() {
        return waterBubblerId;
    }

    public FavoriteBubblerDTO setWaterBubblerId(UUID waterBubblerId) {
        this.waterBubblerId = waterBubblerId;
        return this;
    }

    public Long getWaterBubblerOsmId() {
        return waterBubblerOsmId;
    }

    public FavoriteBubblerDTO setWaterBubblerOsmId(Long waterBubblerOsmId) {
        this.waterBubblerOsmId = waterBubblerOsmId;
        return this;
    }
}
