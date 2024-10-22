package edu.vutfit.ThirstQuest.dto;

public class AuthResponse {
    private String token;
    private String schema;
    private String[] role;

    public AuthResponse() {
    }

    public AuthResponse(String token, String schema, String[] role) {
        this.token = token;
        this.schema = schema;
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

    public String[] getRole() {
        return role;
    }

    public void setRole(String[] role) {
        this.role = role;
    }
}
