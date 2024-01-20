package com.example.dreamland.api.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dreamland.api.model.Pet;
import com.example.dreamland.api.model.jsonconverters.AdopterConverter;
import com.example.dreamland.api.model.jsonconverters.FosterConverter;
import com.example.dreamland.api.model.jsonconverters.PetIdConverter;
import com.example.dreamland.services.AdopterService;
import com.example.dreamland.services.FosterFamilyService;
import com.example.dreamland.services.OwnerService;
import com.example.dreamland.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PetAdoptationControler {
    
    private OwnerService ownerService;
    private AdopterService adopterService;
    private FosterFamilyService fosterService;
    private List<Pet> petList = new ArrayList<>();
    private List<Pet> fosterList = new ArrayList<>();

    @Autowired
    public PetAdoptationControler(OwnerService ownerService, AdopterService adopterService, FosterFamilyService fosterService) {
        this.ownerService = ownerService;
        this.adopterService = adopterService;
        this.fosterService = fosterService;
    }

    @GetMapping("/listNewPet")
    public String listPetPage(Pet pet) {
        return "giveAwayPet";
    }

    @PostMapping("/listNewPet")
    public String listYourPet(Pet pet) {
        String responseMessage = ownerService.newPetRegister(pet);
        return responseMessage;
    }
    
    @GetMapping("/adoptPet")
    public String adoptPetScreen(HttpSession session) {
        if (adopterService.isAdopter(UserService.currentUserID)) {
            
            petList = adopterService.getAdoptablePetList();

            session.setAttribute("petList", petList);
            
            return "adoptPet"; // Load adoptable pet list
        } else {
            return "becomeAdopter"; // Ask for salary
        }
    }

    @GetMapping("/fosterPet")
    public String fosterPetScreen(HttpSession session) {
        int userId = UserService.currentUserID;
        if (fosterService.isFoster(userId)) {
            fosterList = fosterService.getFosterPetList(userId);

            session.setAttribute("fosterList", fosterList);
            return "fosterPet";
        } else {
            return "becomeFosterFamily";
        }
    }

    @PostMapping("/becomeAdopter") // If user getting to the adopter page first time this endpoint will take salary input
    public String becomeAdopter(AdopterConverter adopterConverter) {
        String responseMessage = adopterService.newAdopterRegister(UserService.currentUserID, adopterConverter.getSalary());

        return "profile";
    }

    @PostMapping("/adoptNewPet") //When a user adopts a new pet calls this endpoint with the petid as a json body
    public String adoptNewPet(@RequestBody PetIdConverter petId) {
        String responseMessage = adopterService.adoptPet(UserService.currentUserID, petId);
        return responseMessage;
    }

    @PostMapping("/becomeFosterFamily")
    public String becomeFosterFamily(FosterConverter fosterFamilyConverter) {
        String responseMessage = fosterService.newFosterRegister(UserService.currentUserID, fosterFamilyConverter);
        return "profile";
    }

    @PostMapping("/fosterNewPet")
    public String fosterPet(@RequestBody PetIdConverter petId) {

        return "";
    }

    @GetMapping("/test")
    public void test() {
        fosterService.getFosterPetList(UserService.currentUserID);
    }
}
