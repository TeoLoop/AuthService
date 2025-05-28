package com.auth.auth_server.model;

public enum Role {
    USER,
    ADMIN;

    public String getFullRoleName() {
        return "ROLE_" + this.name();
    }
}
