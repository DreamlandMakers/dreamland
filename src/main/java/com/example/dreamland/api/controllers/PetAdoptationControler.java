package com.example.dreamland.api.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dreamland.api.model.Pet;
import com.example.dreamland.api.model.Report;
import com.example.dreamland.api.model.jsonconverters.AdopterConverter;
import com.example.dreamland.api.model.jsonconverters.FosterConverter;
import com.example.dreamland.services.AdopterService;
import com.example.dreamland.services.FosterFamilyService;
import com.example.dreamland.services.OwnerService;
import com.example.dreamland.services.ReportService;
import com.example.dreamland.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PetAdoptationControler {
    
    private OwnerService ownerService;
    private AdopterService adopterService;
    private FosterFamilyService fosterService;
    private ReportService reportService;
    private List<Pet> petList = new ArrayList<>();
    private List<Pet> fosterList = new ArrayList<>();
    private List<Pet> givenPets = new ArrayList<>();
    private List<Report> reportList = new ArrayList<>();
    @Autowired
    public PetAdoptationControler(OwnerService ownerService, AdopterService adopterService, FosterFamilyService fosterService, ReportService reportService) {
        this.ownerService = ownerService;
        this.adopterService = adopterService;
        this.fosterService = fosterService;
        this.reportService = reportService;
    }

    @GetMapping("/listNewPet")
    public String listPetPage(Pet pet) {
        return "giveAwayPet";
    }

    @PostMapping("/listNewPet")
    public String listYourPet(Pet pet, String[] reportTitles, String[] reportDescriptions, HttpSession session) {
        
        int petId = ownerService.newPetRegister(pet);
        

        if (reportTitles != null && reportDescriptions != null) {
            for (int i = 0; i < reportTitles.length; i++) {
                Report report = new Report();
                report.setId(reportService.getLastId());
                report.setType(reportTitles[i]);
                report.setDescription(reportDescriptions[i]);
                reportService.addReport(petId, report);
            }
        }
        givenPets = ownerService.getGivenPets(UserService.currentUserID);

        session.setAttribute("givenPets", givenPets);

        return "givenPets";
    }
    @PostMapping("/listExistingPet")
    public String listExistingPet(String petId, HttpSession session) {
        
        String responseMessage = ownerService.existingPetAbondon(Integer.parseInt(petId));
    
        givenPets = ownerService.getGivenPets(UserService.currentUserID);

        session.setAttribute("givenPets", givenPets);

        return "givenPets";
    }
    @PostMapping("/listFosteredPet")
    public String listFosteredPet(String petId, HttpSession session) {
        
        String responseMessage = ownerService.fosteredPetAbondon(Integer.parseInt(petId));
    
        givenPets = ownerService.getGivenPets(UserService.currentUserID);

        session.setAttribute("givenPets", givenPets);

        return "givenPets";
    }

    @GetMapping("/listMyPets")
    public String listMyPets(HttpSession session) {

        givenPets = ownerService.getGivenPets(UserService.currentUserID);

        session.setAttribute("givenPets", givenPets);

        return "givenPets";
    }
    @GetMapping("/listMyAdoptedPets")
    public String listMyAdoptedPets(HttpSession session) {

        List<Pet> adoptedPets = adopterService.getAdoptedPets(UserService.currentUserID);

        session.setAttribute("adoptedPets", adoptedPets);

        return "adoptedPets";
    }
    @GetMapping("/listMyFosteredPets")
    public String listMyFosteredPets(HttpSession session) {

        fosterList = fosterService.getFosteredPets(UserService.currentUserID);

        session.setAttribute("fosterList", fosterList);

        return "fosteredPets";
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
    public String adoptNewPet(String petId, HttpSession session) {
        String responseMessage = adopterService.adoptPet(UserService.currentUserID, petId);
        if(responseMessage.equals("Pet adopted")){
            petList = adopterService.getAdoptablePetList();

            session.setAttribute("petList", petList);
            
            return "adoptPet"; // Load adoptable pet list
        }
        return "adoptPet";
    }

    @PostMapping("/becomeFosterFamily")
    public String becomeFosterFamily(FosterConverter fosterFamilyConverter) {
        String responseMessage = fosterService.newFosterRegister(UserService.currentUserID, fosterFamilyConverter);
        return "profile";
    }

    @PostMapping("/fosterNewPet")
    public String fosterPet(String petId, HttpSession session) {
        int userId=UserService.currentUserID;
        String responseMessage = fosterService.fosterPet(userId, petId);
        if (responseMessage.equals("Pet is forwarded to the foster family")) {
            fosterList = fosterService.getFosterPetList(userId);

            session.setAttribute("fosterList", fosterList);
            return "fosterPet";
        }
        return  "fosterPet";
    }
    @PostMapping("/undoGiveAway") //When a user adopts a new pet calls this endpoint with the petid as a json body
    public String undoGiveAway(String petId, HttpSession session) {
        String responseMessage = adopterService.undoGiving(Integer.parseInt(petId));
        if(responseMessage.equals("Pet resurrected")){
            givenPets = ownerService.getGivenPets(UserService.currentUserID);

            session.setAttribute("givenPets", givenPets);
            
            return "givenPets"; // Load adoptable pet list
        }
        return "givenPets";
    }

    @PostMapping("/editPet")
    public String editPetScreen(String petId, HttpSession session) {
            Pet pet = ownerService.getPet(Integer.parseInt(petId));

            session.setAttribute("pet", pet);
            
            return "editPet"; // Load adoptable pet list
    }

    @PostMapping("/updatePet")
    public String updatePet(Pet pet, HttpSession session) {
            ownerService.updatePet(pet);

            givenPets = ownerService.getGivenPets(UserService.currentUserID);

            session.setAttribute("givenPets", givenPets);
            
            return "givenPets"; // Load adoptable pet list
    }

    @PostMapping("/showReport")
    public String showReport(String petId, HttpSession session,String prevPage) {
            reportList = adopterService.getPetReports(Integer.parseInt(petId));

            session.setAttribute("reportList", reportList);

            session.setAttribute("prevPage", prevPage);

            return "reportPage"; // Load adoptable pet list
    }

    @GetMapping("/test")
    public void test() {
        fosterService.getFosterPetList(UserService.currentUserID);
    }
}
