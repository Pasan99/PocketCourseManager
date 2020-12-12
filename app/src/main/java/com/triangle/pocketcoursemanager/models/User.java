package com.triangle.pocketcoursemanager.models;

public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private String description;
    private String userType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User(long id, String name, String email, String password, String description, String userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.description = description;
        this.userType = userType;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
