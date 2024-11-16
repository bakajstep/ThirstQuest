package edu.vutfit.ThirstQuest.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Review {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "water_bubbler_id")
    private WaterBubbler waterBubbler;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public WaterBubbler getWaterBubbler() {
        return waterBubbler;
    }

    public void setWaterBubbler(WaterBubbler waterBubbler) {
        this.waterBubbler = waterBubbler;
    }
}
