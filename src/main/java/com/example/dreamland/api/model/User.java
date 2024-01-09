package com.example.dreamland.api.model;

public class User {
    
    private String userName;
    private String password;
    private String email;
    private String name;
    private String surName;
    private String birthDate;
    private String sex;
    private int numberOfPets;

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getSurName() {
        return this.surName;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public String getSex() {
        return this.sex;
    }

    public int getNumberOfPets() {
        return this.numberOfPets;
    }

    public String getEmail() {
        return this.email;
    }
}
