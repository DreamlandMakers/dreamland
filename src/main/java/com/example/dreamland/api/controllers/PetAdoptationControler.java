package com.example.dreamland.api.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dreamland.api.model.Pet;
import com.example.dreamland.api.model.PetReport;
import com.example.dreamland.api.model.Report;
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
    List<PetReport> givenPetReports = new ArrayList<>();
    List<PetReport> adoptedPetReports = new ArrayList<>();
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
        if(ownerService.getNumPets(UserService.currentUserID) <=0){
            //you cant give more than the pets you own
                String alertMessage = "Pet giving failed." + "You dont have any pets to give." +"'\n'You will be directed to profile page!";
                String script = String.format("'%s'", alertMessage);
                session.setAttribute("errorMessage", script);
                return "unable";
        }

        int petId = ownerService.newPetRegister(pet);
        

        if (reportTitles != null && reportDescriptions != null) {
            for (int i = 0; i < reportTitles.length; i++) {
                Report report = new Report();
                report.setId(i+1);
                report.setType(reportTitles[i]);
                report.setDescription(reportDescriptions[i]);
                reportService.addReport(petId, report);
            }
        }
        givenPetReports= ownerService.getGivenPetsWithReports(UserService.currentUserID);
        session.setAttribute("givenPetReports", givenPetReports);

        return "givenPets";
    }
    @PostMapping("/listExistingPet")
    public String listExistingPet(String petId, HttpSession session) {
        
        String responseMessage = ownerService.existingPetAbondon(Integer.parseInt(petId));
        if(responseMessage.equals("Pet updated")){
            ownerService.decreaseNumPets(UserService.currentUserID);
        }
        givenPetReports= ownerService.getGivenPetsWithReports(UserService.currentUserID);
        session.setAttribute("givenPetReports", givenPetReports);

        return "givenPets";
    }
    @PostMapping("/listFosteredPet")
    public String listFosteredPet(String petId, HttpSession session) {
        
        String responseMessage = ownerService.fosteredPetAbondon(Integer.parseInt(petId));
        if(responseMessage.equals("Pet updated")){
            ownerService.decreaseNumPets(UserService.currentUserID);
            givenPetReports= ownerService.getGivenPetsWithReports(UserService.currentUserID);
            session.setAttribute("givenPetReports", givenPetReports);
            return "givenPets";
        }else{
            String alertMessage = "Pet giving failed. " + responseMessage+"'\n'You will be directed to profile page!";
            String script = String.format("'%s'", alertMessage);
            session.setAttribute("errorMessage", script);
            return "unable";
        }

    }

    @GetMapping("/listMyPets")
    public String listMyPets(HttpSession session) {

        
        givenPetReports= ownerService.getGivenPetsWithReports(UserService.currentUserID);
        session.setAttribute("givenPetReports", givenPetReports);

        return "givenPets";
    }
    @GetMapping("/listMyAdoptedPets")
    public String listMyAdoptedPets(HttpSession session) {
        List<Pet> adoptedPets = adopterService.getAdoptedPets(UserService.currentUserID);
        int numofPets=adoptedPets.size();
        adoptedPetReports.clear();
        for(int i =0;i<numofPets;i++){
            int petId=adoptedPets.get(i).getId();
            PetReport pr=new PetReport();
            pr.setPet(adoptedPets.get(i));
            List<Report> r= adopterService.getPetReports(petId);
            pr.setReports(r);
            adoptedPetReports.add(pr);
        }
        session.setAttribute("adoptedPetReports", adoptedPetReports);

        return "adoptedPets";
    }
    @GetMapping("/listMyFosteredPets")
    public String listMyFosteredPets(HttpSession session) {

        fosterList = fosterService.getFosteredPets(UserService.currentUserID);
        int numofPets=fosterList.size();
        adoptedPetReports.clear();
        for(int i =0;i<numofPets;i++){
            int petId=fosterList.get(i).getId();
            PetReport pr=new PetReport();
            pr.setPet(fosterList.get(i));
            List<Report> r= adopterService.getPetReports(petId);
            pr.setReports(r);
            adoptedPetReports.add(pr);
        }
        session.setAttribute("fosterList", adoptedPetReports);

        return "fosteredPets";
    }
    @GetMapping("/adoptPet")
    public String adoptPetScreen(HttpSession session) {
        if (adopterService.isAdopter(UserService.currentUserID)) {
            
            petList = adopterService.getAdoptablePetList(UserService.currentUserID);
            int numofPets=petList.size();
            adoptedPetReports.clear();
            for(int i =0;i<numofPets;i++){
            int petId=petList.get(i).getId();
            PetReport pr=new PetReport();
            pr.setPet(petList.get(i));
            List<Report> r= adopterService.getPetReports(petId);
            pr.setReports(r);
            adoptedPetReports.add(pr);
        }
            session.setAttribute("petList", adoptedPetReports);
            
            return "adoptPet"; // Load adoptable pet list
        } else {
            session.setAttribute("adopter", false);
            if (fosterService.isFoster(UserService.currentUserID)) {
                session.setAttribute("foster", true);
                session.setAttribute("fosterSalary", fosterService.fosterSalary(UserService.currentUserID));
                return "wannaBeBoth";
            }
            return "becomeAdopter"; // Ask for salary
        }
    }

    @GetMapping("/fosterPet")
    public String fosterPetScreen(HttpSession session) {
        int userId = UserService.currentUserID;
        if (fosterService.isFoster(userId)) {

            fosterList = fosterService.getFosterPetList(userId);

            int numofPets=fosterList.size();
            adoptedPetReports.clear();
            for(int i =0;i<numofPets;i++){
            Pet p = fosterList.get(i);
            int petId=p.getId();
            PetReport pr=new PetReport();
            pr.setPet(p);
            List<Report> r= adopterService.getPetReports(petId);
            pr.setReports(r);
            adoptedPetReports.add(pr);
            }
            session.setAttribute("fosterList", adoptedPetReports);
            return "fosterPet";
        } else {
            session.setAttribute("foster", false);
            if (adopterService.isAdopter(UserService.currentUserID)) {
                session.setAttribute("adopter", true);
                session.setAttribute("adopterSalary", adopterService.adopterSalary(UserService.currentUserID));
                return "wannaBeBoth";
        }
        return "becomeFosterFamily";
    }
    }

    @PostMapping("/becomeAdopter") // If user getting to the adopter page first time this endpoint will take salary input
    public String becomeAdopter(String salary) {
        String responseMessage = adopterService.newAdopterRegister(UserService.currentUserID, Double.parseDouble(salary));

        return "profile";
    }

    @PostMapping("/adoptNewPet")
public String adoptNewPet(String petId, HttpSession session) {
    String responseMessage = adopterService.adoptPet(UserService.currentUserID, Integer.parseInt(petId));

    if (responseMessage.equals("Pet adopted")) {
        // Find and remove the adopted pet from the list
        ownerService.increaseNumPets(UserService.currentUserID);
        Iterator<PetReport> iterator = adoptedPetReports.iterator();
        while (iterator.hasNext()) {
            PetReport petReport = iterator.next();
            if (petReport.getPet().getId() == Integer.parseInt(petId)) {
                iterator.remove();
                break;
            }
        }

        session.setAttribute("petList", adoptedPetReports);
        return "adoptPet";
    }
    else{
        String alertMessage = "Pet adoption failed." + responseMessage +"'\n'You will be directed to profile page!";
        String script = String.format("'%s'", alertMessage);
        session.setAttribute("errorMessage", script);
        return "unable";
    }
    
}


    @PostMapping("/becomeFosterFamily")
    public String becomeFosterFamily(String salary, String type) {
        String responseMessage = fosterService.newFosterRegister(UserService.currentUserID, Double.parseDouble(salary), type);
        return "profile";
    }

    @PostMapping("/fosterNewPet")
    public String fosterPet(String petId, HttpSession session) {
        int userId=UserService.currentUserID;
        String responseMessage = fosterService.fosterPet(userId, Integer.parseInt(petId));
        if (responseMessage.equals("Pet is forwarded to the foster family")) {
            ownerService.increaseNumPets(UserService.currentUserID);
            Iterator<PetReport> iterator = adoptedPetReports.iterator();
            while (iterator.hasNext()) {
            PetReport petReport = iterator.next();
            if (petReport.getPet().getId() == Integer.parseInt(petId)) {
                iterator.remove();
                break;
            }
            
        }
        session.setAttribute("fosterList", adoptedPetReports);
        return  "fosterPet";
        }else{
            String alertMessage = "Pet fostering failed." + responseMessage +"'\n'You will be directed to profile page!";
            String script = String.format("'%s'", alertMessage);
            session.setAttribute("errorMessage", script);
            return "unable";
        }
        
    }
    @PostMapping("/undoGiveAway") //When a user adopts a new pet calls this endpoint with the petid as a json body
    public String undoGiveAway(String petId, HttpSession session) {
        String responseMessage = ownerService.undoGiving(Integer.parseInt(petId));
        if(responseMessage.equals("Pet resurrected")){
            ownerService.increaseNumPets(UserService.currentUserID);
            givenPetReports= ownerService.getGivenPetsWithReports(UserService.currentUserID);
            session.setAttribute("givenPetReports", givenPetReports);
            
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
    public String updatePet(Pet pet, String[] reportTitles, String[] reportDescriptions, HttpSession session) {
            ownerService.updatePet(pet);

            if (reportTitles != null && reportDescriptions != null) {
                for (int i = 0; i < reportTitles.length; i++) {
                    Report report = new Report();
                    report.setId(i+ reportService.getLastId(pet.getId()));
                    report.setType(reportTitles[i]);
                    report.setDescription(reportDescriptions[i]);
                    reportService.addReport(pet.getId(), report);
                }
            }

            
            //givenPetReports= ownerService.getGivenPetsWithReports(UserService.currentUserID);
            //session.setAttribute("givenPetReports", givenPetReports);
            
            return "profile"; // Load adoptable pet list
    }

    @PostMapping("/showReport")
    public String showReport(String petId, HttpSession session) {
            reportList = adopterService.getPetReports(Integer.parseInt(petId));

            session.setAttribute("reportList", reportList);

            session.setAttribute("pet_id", Integer.parseInt(petId));

            return "reportPage"; // Load adoptable pet list
    }
    @PostMapping("/editReport")
    public String editReport(String reportId, String petId, HttpSession session) {
            Report report = reportService.getReport(Integer.parseInt(reportId),Integer.parseInt(petId));
            
            session.setAttribute("report", report);
            session.setAttribute("petId", petId);

            return "updateReportPage"; // Load adoptable pet list
    }

    @PostMapping("/updateReport")
    public String updateReport(Report report, String petId, HttpSession session) {
        reportService.updateReport(report, Integer.parseInt(petId));

            reportList = adopterService.getPetReports(Integer.parseInt(petId));
            session.setAttribute("reportList", reportList);

            return "reportPage"; // Load adoptable pet list
    }

    @PostMapping("/deleteReport")
    public String deleteReport(String reportId, String petId, HttpSession session) {
        reportService.deleteReport(Integer.parseInt(reportId), Integer.parseInt(petId));

            reportList = adopterService.getPetReports(Integer.parseInt(petId));
            session.setAttribute("reportList", reportList);

            return "reportPage"; // Load adoptable pet list
    }
    @GetMapping("/test")
    public void test() {
        fosterService.getFosterPetList(UserService.currentUserID);
    }
}
