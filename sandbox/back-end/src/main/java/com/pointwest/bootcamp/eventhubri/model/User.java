package com.pointwest.bootcamp.eventhubri.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users", 
        indexes = {
        @Index(name = "idx_user_name", columnList = "name") 
    })
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    private String userId;

    @Column(nullable = false, length = 100)
    private String name;
    private String email;

    public User() {
    }

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
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
