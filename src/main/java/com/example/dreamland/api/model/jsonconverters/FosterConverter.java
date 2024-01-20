package com.example.dreamland.api.model.jsonconverters;

public class FosterConverter {
    
    private String type;
    private double salary;

    public String getType() {
        return this.type;
    }

    public double getSalary() {
        return this.salary;
    }
    public void setType(String type) {
        this.type=type;
    }

    public void setSalary(int salary) {
        this.salary=salary;
    }
}
