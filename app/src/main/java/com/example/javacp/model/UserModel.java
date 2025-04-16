package com.example.javacp.model;


public class UserModel {

    private String userId;
    private String name;

    // Constructor
    public UserModel(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

