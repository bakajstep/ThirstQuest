package edu.vutfit.ThirstQuest.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Photo {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String url;

    private String desc;

    @ManyToOne
    @JoinColumn(name = "user_id") // foreign key to User
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
