package edu.vutfit.ThirstQuest.dto;

import edu.vutfit.ThirstQuest.model.VoteType;

import java.util.UUID;


public class ReviewDTO {
    private UUID id;
    private VoteType voteType;
    private UUID userId;
    private UUID waterBubblerId;
    private Long waterBubblerOsmId;

    public UUID getId() {
        return id;
    }

    public ReviewDTO setId(UUID id) {
        this.id = id;
        return this;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public ReviewDTO setVoteType(VoteType voteType) {
        this.voteType = voteType;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public ReviewDTO setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public UUID getWaterBubblerId() {
        return waterBubblerId;
    }

    public ReviewDTO setWaterBubblerId(UUID waterBubblerId) {
        this.waterBubblerId = waterBubblerId;
        return this;
    }

    public Long getWaterBubblerOsmId() {
        return waterBubblerOsmId;
    }

    public ReviewDTO setWaterBubblerOsmId(Long waterBubblerOsmId) {
        this.waterBubblerOsmId = waterBubblerOsmId;
        return this;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", voteType=" + voteType +
                ", userId=" + userId +
                ", waterBubblerId=" + waterBubblerId +
                ", waterBubblerOsmId=" + waterBubblerOsmId +
                '}';
    }
}

