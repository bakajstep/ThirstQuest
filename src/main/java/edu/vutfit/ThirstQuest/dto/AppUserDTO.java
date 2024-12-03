package edu.vutfit.ThirstQuest.dto;

import java.util.UUID;

public class AppUserDTO {

    private UUID id;
    private String email;
    private String username;
    private boolean authByGoogle;
    private int role;
    private String profilePicture;

    // Constructors
    public AppUserDTO() {
    }

    public AppUserDTO(UUID id, String email, String username, boolean authByGoogle, int role, String profilePicture) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.authByGoogle = authByGoogle;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    // Fluent Setters
    public AppUserDTO setId(UUID id) {
        this.id = id;
        return this;
    }

    public AppUserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public AppUserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public AppUserDTO setAuthByGoogle(boolean authByGoogle) {
        this.authByGoogle = authByGoogle;
        return this;
    }

    public AppUserDTO setRole(int role) {
        this.role = role;
        return this;
    }

    public AppUserDTO setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAuthByGoogle() {
        return authByGoogle;
    }

    public int getRole() {
        return role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}