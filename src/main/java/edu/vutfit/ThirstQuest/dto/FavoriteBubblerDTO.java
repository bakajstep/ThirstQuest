package edu.vutfit.ThirstQuest.dto;

import java.util.UUID;

public class FavoriteBubblerDTO {
    private UUID id;
    private Long osmId;

    public UUID getId() {
        return id;
    }

    public FavoriteBubblerDTO setId(UUID waterBubblerId) {
        this.id = waterBubblerId;
        return this;
    }

    public Long getOsmId() {
        return osmId;
    }

    public FavoriteBubblerDTO setOsmId(Long waterBubblerOsmId) {
        this.osmId = waterBubblerOsmId;
        return this;
    }
}
