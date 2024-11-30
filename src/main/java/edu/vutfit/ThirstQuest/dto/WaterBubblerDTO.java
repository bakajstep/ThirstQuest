package edu.vutfit.ThirstQuest.dto;

import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.model.WaterBubbler;

import java.util.List;
import java.util.UUID;

public class WaterBubblerDTO {
    private UUID id;
    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private List<PhotoDTO> photos;
    private UUID userId;
    private Long osmId;
    private int upvoteCount;
    private int downvoteCount;

    public WaterBubblerDTO() {
        // Default constructor
    }

    public UUID getId() {
        return id;
    }

    public WaterBubblerDTO setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WaterBubblerDTO setName(String name) {
        this.name = name;
        return this;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public WaterBubblerDTO setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public WaterBubblerDTO setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public WaterBubblerDTO setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public WaterBubblerDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getOsmId() {
        return this.osmId;
    }

    public WaterBubblerDTO setOsmId(Long osmId) {
        this.osmId = osmId;
        return this;
    }

    public List<PhotoDTO> getPhotos() {
        return photos;
    }

    public WaterBubblerDTO setPhotos(List<PhotoDTO> photos) {
        this.photos = photos;
        return this;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public WaterBubblerDTO setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
        return this;
    }

    public int getDownvoteCount() {
        return downvoteCount;
    }

    public WaterBubblerDTO setDownvoteCount(int downvoteCount) {
        this.downvoteCount = downvoteCount;
        return this;
    }
}
