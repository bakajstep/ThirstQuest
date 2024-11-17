package edu.vutfit.ThirstQuest.dto;

import java.util.UUID;

public class PhotoDTO {
    private UUID id;
    private String name;
    private String url;
    private UUID userId;
    private UUID waterBubblerId;
    private Long waterBubblerOsmId;

    public UUID getId() {
        return id;
    }

    public PhotoDTO setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PhotoDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PhotoDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public PhotoDTO setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public UUID getWaterBubblerId() {
        return waterBubblerId;
    }

    public PhotoDTO setWaterBubblerId(UUID waterBubblerId) {
        this.waterBubblerId = waterBubblerId;
        return this;
    }

    public Long getWaterBubblerOsmId() {
        return waterBubblerOsmId;
    }

    public PhotoDTO setWaterBubblerOsmId(Long waterBubblerOsmId) {
        this.waterBubblerOsmId = waterBubblerOsmId;
        return this;
    }

    @Override
    public String toString() {
        return "PhotoDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", userId=" + userId +
                ", waterBubblerId=" + waterBubblerId +
                ", waterBubblerOsmId=" + waterBubblerOsmId +
                '}';
    }
}

