package com.example.dreamland.api.model;

public class Pet {

    private String name;
    private int age;
    private String type;
    private double averageExpense;
    private String breed;
    private int yearOfOwnership;  

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public String getType() {
        return this.type;
    }

    public double getAverageExpense() {
        return this.averageExpense;
    }

    public String getBreed() {
        return this.breed;
    }

    public int getYearOfOwnership() {
        return this.yearOfOwnership;
    }
}
