package com.pointwest.bootcamp.eventhubri.dto;

import com.pointwest.bootcamp.eventhubri.model.User;

public class UserDto {
    private String userId;
    private String name;
    private String email;

    public UserDto() {
    }

    public UserDto(User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.name = user.getName();
            this.email = user.getEmail();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
