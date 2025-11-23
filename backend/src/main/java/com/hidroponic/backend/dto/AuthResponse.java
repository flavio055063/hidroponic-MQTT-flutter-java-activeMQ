package com.hidroponic.backend.dto;

public class AuthResponse {
    private String token;
    private Long equipmentId;
    private String username;
    private boolean admin;

    public AuthResponse(String token, Long equipmentId, String username, boolean admin) {
        this.token = token;
        this.equipmentId = equipmentId;
        this.username = username;
        this.admin = admin;
    }

    public String getToken() {
        return token;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }
}
