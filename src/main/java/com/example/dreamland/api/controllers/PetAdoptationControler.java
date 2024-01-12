package com.example.dreamland.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamland.api.model.Pet;
import com.example.dreamland.services.PetAdoptationService;

@RestController
public class PetAdoptationControler {
    
    private PetAdoptationService adoptationService;

    @Autowired
    public PetAdoptationControler(PetAdoptationService adoptationService) {
        this.adoptationService = adoptationService;
    }

    @PostMapping("/listPet")
    public String listYourPet(@RequestBody Pet pet) {
        String responseMessage = adoptationService.newPetRegister(pet);
        return responseMessage;
    }
    
}
