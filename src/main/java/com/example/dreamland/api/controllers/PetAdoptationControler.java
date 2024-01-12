package com.example.dreamland.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dreamland.api.model.Pet;
import com.example.dreamland.api.model.jsonconverters.AdopterConverter;
import com.example.dreamland.api.model.jsonconverters.PetIdConverter;
import com.example.dreamland.services.AdopterService;
import com.example.dreamland.services.OwnerService;
import com.example.dreamland.services.UserService;

@RestController
public class PetAdoptationControler {
    
    private OwnerService ownerService;
    private AdopterService adopterService;

    @Autowired
    public PetAdoptationControler(OwnerService ownerService, AdopterService adopterService) {
        this.ownerService = ownerService;
        this.adopterService = adopterService;
    }

    @PostMapping("/listNewPet")
    public String listYourPet(@RequestBody Pet pet) {
        String responseMessage = ownerService.newPetRegister(pet);
        return responseMessage;
    }
    
    @GetMapping("/adoptPet")
    public String adoptPetScreen() {
        if (adopterService.isAdopter(UserService.currentUserID)) {
            return "Adoptable Pet List"; // Load adoptable pet list
        } else {
            return "Become Adopter Screen"; // Ask for salary
        }
    }

    @PostMapping("/becomeAdopter") // If user getting to the adopter page first time this endpoint will take salary input
    public String becomeAdopter(@RequestBody AdopterConverter adopterConverter) {
        String responseMessage = adopterService.newAdopterRegister(UserService.currentUserID, adopterConverter.getSalary());
        return responseMessage;
    }

    @PostMapping("/adoptNewPet") //When a user adopts a new pet calls this endpoint with the petid as a json body
    public String adoptNewPet(@RequestBody PetIdConverter petId) {
        String responseMessage = adopterService.adoptPet(UserService.currentUserID, petId);
        return responseMessage;
    }
}
