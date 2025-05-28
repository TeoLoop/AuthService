package com.auth.auth_server.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String name;
    private String lastName;
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String email, String name, String lastName, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
    }
}
