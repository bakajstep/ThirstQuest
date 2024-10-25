package edu.vutfit.ThirstQuest.dto;

import edu.vutfit.ThirstQuest.model.AppUser;

public class AuthResponse {
    private String token;
    private String schema;
    private AppUser user;
    private String[] role;

    public AuthResponse() {
    }

    public AuthResponse(String token, String schema, AppUser user, String[] role) {
        this.token = token;
        this.schema = schema;
        this.user = user;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String[] getRole() {
        return role;
    }

    public void setRole(String[] role) {
        this.role = role;
    }
}
