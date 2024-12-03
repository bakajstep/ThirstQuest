package edu.vutfit.ThirstQuest.dto;

public class AuthResponse {
    private String token;
    private String schema;
    private AppUserDTO user;
    private String[] role;

    public AuthResponse() {
    }

    public AuthResponse(String token, String schema, AppUserDTO user, String[] role) {
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

    public AppUserDTO getUser() {
        return user;
    }

    public void setUser(AppUserDTO user) {
        this.user = user;
    }

    public String[] getRole() {
        return role;
    }

    public void setRole(String[] role) {
        this.role = role;
    }
}
