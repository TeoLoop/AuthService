package com.auth.auth_server.dto;

import lombok.Data;

@Data
public class UserDto {

    private String name;
    private String lastName;
    private String email;
    private String role;

    public UserDto() {
    }

}
