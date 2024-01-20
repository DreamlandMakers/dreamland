package com.example.dreamland.api.model;

import java.util.List;
import java.util.ArrayList;

public class PetList {

    private List<Pet> petList = new ArrayList<>();

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }
    
}
