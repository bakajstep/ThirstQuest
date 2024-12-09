package edu.vutfit.ThirstQuest.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class WaterBubbler {

    @Id
    @GeneratedValue
    private UUID id;

    private Long openStreetId;

    private String name;

    private String description;

    private double latitude;

    private double longitude;

    @ManyToOne
    @JoinColumn(name = "user_id") // foreign key to User
    private AppUser user;

    @OneToMany(mappedBy = "waterBubbler", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "waterBubbler")
    private List<Photo> photos;

    @ManyToMany(mappedBy = "favoriteBubblers")
    private Set<AppUser> usersWhoFavorited;

    public UUID getId() {
        return id;
    }

    public WaterBubbler setId(UUID id) {
        this.id = id;
        return this;
    }

    public Long getOpenStreetId() {
        return openStreetId;
    }

    public WaterBubbler setOpenStreetId(Long openStreetId) {
        this.openStreetId = openStreetId;
        return this;
    }

    public String getName() {
        return name;
    }

    public WaterBubbler setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WaterBubbler setDescription(String desc) {
        this.description = desc;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public WaterBubbler setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public WaterBubbler setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public AppUser getUser() {
        return user;
    }

    public WaterBubbler setUser(AppUser appUser) {
        this.user = appUser;
        return this;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public WaterBubbler setReviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public WaterBubbler setPhotos(List<Photo> photos) {
        this.photos = photos;
        return this;
    }

    public Set<AppUser> getUsersWhoFavorited() {
        return usersWhoFavorited;
    }

    public void setUsersWhoFavorited(Set<AppUser> usersWhoFavorited) {
        this.usersWhoFavorited = usersWhoFavorited;
    }
}
