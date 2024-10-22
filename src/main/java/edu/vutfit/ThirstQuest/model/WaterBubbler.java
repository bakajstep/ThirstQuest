package edu.vutfit.ThirstQuest.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class WaterBubbler {

    @Id
    @GeneratedValue
    private UUID id;

    private Long openStreetId;

    private String name;

    private double latitude;

    private double longitude;

    @ManyToOne
    @JoinColumn(name = "user_id") // foreign key to User
    private AppUser user;

    @OneToMany(mappedBy = "waterBubbler")
    private List<Review> reviews;

    @OneToMany(mappedBy = "waterBubbler")
    private List<Photo> photos;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getOpenStreetId() {
        return openStreetId;
    }

    public void setOpenStreetId(Long openStreetId) {
        this.openStreetId = openStreetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
