package edu.vutfit.ThirstQuest.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class AppUser {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;

    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int role;

    @Column(length = 512)
    private String profilePicture;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    private List<WaterBubbler> waterBubblers;

    @OneToMany(mappedBy = "user")
    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<WaterBubbler> getWaterBubblers() {
        return waterBubblers;
    }

    public void setWaterBubblers(List<WaterBubbler> waterBubblers) {
        this.waterBubblers = waterBubblers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int priviliages) {
        this.role = priviliages;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
