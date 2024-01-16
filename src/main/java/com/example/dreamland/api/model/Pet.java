package com.example.dreamland.api.model;

public class Pet {

    private int id;
    private String name;
    private int age;
    private String type;
    private double averageExpense;
    private String breed;
    private int yearOfOwnership;  

    public void setAverageExpense(double averageExpense) {
        this.averageExpense = averageExpense;
    }

    public void setYearOfOwnership(int yearOfOwnership) {
        this.yearOfOwnership = yearOfOwnership;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public String getType() {
        return this.type;
    }

    public double getCost() {
        return this.averageExpense;
    }

    public String getBreed() {
        return this.breed;
    }

    public int getYearOfOwnership() {
        return this.yearOfOwnership;
    }

    public int getPetId() {
        return this.id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCost(double cost) {
        this.averageExpense = cost;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }
}
