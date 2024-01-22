package com.example.dreamland.api.model;

public class Pet {

    private int id;
    
    private String name;
    private int age;
    private String type;
    private double averageExpense;
    private String breed;
    private int yearOfOwnership;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getAverageExpense() {
        return averageExpense;
    }
    public void setAverageExpense(double averageExpense) {
        this.averageExpense = averageExpense;
    }
    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }
    public int getYearOfOwnership() {
        return yearOfOwnership;
    }
    public void setYearOfOwnership(int yearOfOwnership) {
        this.yearOfOwnership = yearOfOwnership;
    }
    
}