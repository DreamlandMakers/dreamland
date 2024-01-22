package com.example.dreamland.api.model;

import java.util.List;

public class PetReport {

    private Pet pet;
    private List<Report> pet_reports;
    
    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    // Getter and setter for name
    public List<Report> getReports() {
        return pet_reports;
    }

    public void setReports(List<Report> pet_reports) {
        this.pet_reports = pet_reports;
    }

}
