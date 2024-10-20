package edu.vutfit.ThirstQuest.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Review {

    @Id
    @GeneratedValue
    private UUID id;

    private int rating;

    private String text;

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public WaterBubbler getWaterBubbler() {
        return waterBubbler;
    }

    public void setWaterBubbler(WaterBubbler waterBubbler) {
        this.waterBubbler = waterBubbler;
    }
}
