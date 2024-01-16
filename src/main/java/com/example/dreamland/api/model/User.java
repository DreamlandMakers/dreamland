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

    public String getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurName() {
        return surName;
    }
    public void setSurName(String surName) {
        this.surName = surName;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public int getNumberOfPets() {
        return numberOfPets;
    }
    public void setNumberOfPets(int numberOfPets) {
        this.numberOfPets = numberOfPets;
    }


}
